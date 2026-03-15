package com.allobank.allobackendtest.strategy.impl;

import com.allobank.allobackendtest.exception.ExternalServiceException;
import com.allobank.allobackendtest.strategy.IDRDataFetcher;
import com.allobank.allobackendtest.util.SpreadFactorCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Component("latest_idr_rates")
@RequiredArgsConstructor
@Slf4j
public class LatestRatesStrategy implements IDRDataFetcher {
    private final WebClient webClient;
    private final SpreadFactorCalculator spreadFactorCal;

    @Override
    public Mono<Object> fetchData() {
        log.info("Fetching latest rates with base IDR from base API...");
        return webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .map(body -> new ExternalServiceException(
                                        "API 4xx error: " + body
                                )))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class)
                                .map(body -> new ExternalServiceException(
                                        "API 5xx error: "+ body
                                )))
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(35))
                .onErrorMap(e -> new ExternalServiceException("API unavailable", e))
                .map(response -> {
                    Map<String, Object> rates = (Map<String, Object>) response.get("rates");
                    Double rateUsd = (Double) rates.get("USD");
                    if (rateUsd != null) {
                        double spreadFactor = spreadFactorCal.calculate();
                        double usdBuySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);
                        response.put("USD_BuySpread_IDR", usdBuySpreadIdr);
                        log.info("Calculated USD_BuySpread_IDR: {} using rate_USD: {}", usdBuySpreadIdr, rateUsd);
                    }
                    return response;
                });
    }

    @Override
    public boolean supports(String type) {
        return "latest_idr_rates".equals(type);
    }
}
