package com.allobank.strategy.impl;

import com.allobank.config.FrankfurterApiProperties;
import com.allobank.dto.LatestRatesResponse;
import com.allobank.strategy.IDRDataFetcher;
import com.allobank.util.SpreadCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class LatestIdrRatesStrategy implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "latest_idr_rates";
    private static final String USD_CURRENCY = "USD";

    private final WebClient webClient;
    private final FrankfurterApiProperties apiProperties;

    @Value("${github.username}")
    private String githubUsername;

    @Override
    public Mono<Object> fetchData() {
        log.info("Fetching latest IDR rates from Frankfurter API");
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("base", "IDR")
                        .build())
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .map(this::enrichWithSpread)
                .cast(Object.class)
                .doOnError(error -> log.error("Error fetching latest IDR rates", error));
    }

    private LatestRatesResponse enrichWithSpread(LatestRatesResponse response) {
        if (response == null || response.getRates() == null) {
            throw new IllegalStateException("Invalid response from API");
        }

        BigDecimal usdRate = response.getRates().get(USD_CURRENCY);
        if (usdRate == null) {
            throw new IllegalStateException("USD rate not found in response");
        }

        BigDecimal spreadFactor = SpreadCalculator.calculateSpreadFactor(githubUsername);
        BigDecimal usdBuySpreadIdr = SpreadCalculator.calculateUsdBuySpreadIdr(usdRate, spreadFactor);

        log.debug("Calculated spread factor: {}, USD_BuySpread_IDR: {}", spreadFactor, usdBuySpreadIdr);

        return LatestRatesResponse.builder()
                .amount(response.getAmount())
                .base(response.getBase())
                .date(response.getDate())
                .rates(response.getRates())
                .usdBuySpreadIdr(usdBuySpreadIdr)
                .build();
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }
}

