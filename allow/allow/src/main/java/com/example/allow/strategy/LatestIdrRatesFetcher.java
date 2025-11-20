package com.example.allow.strategy;


import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.allow.model.LatestRatesResponse;
import com.example.allow.model.UsdBuySpreadWrapper;
import com.example.allow.util.SpreadCalculator;

import reactor.core.publisher.Mono;

@Component("latest_idr_rates")
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;
    private final SpreadCalculator spreadCalculator;

    public LatestIdrRatesFetcher(WebClient webClient, SpreadCalculator spreadCalculator) {
        this.webClient = webClient;
        this.spreadCalculator = spreadCalculator;
    }

    @Override
    public Mono<Object> fetchData() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("base", "IDR")
                        .build())
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .map(response -> {
                    Map<String, Double> rates = response.getRates();
                    Double rateUsd = rates != null ? rates.get("USD") : null;

                    if (rateUsd == null || rateUsd <= 0) {
                        throw new IllegalStateException("USD rate is missing or invalid in latest rates");
                    }

                    double spreadFactor = spreadCalculator.getSpreadFactor();
                    double usdBuySpreadIdr = (1.0 / rateUsd) * (1.0 + spreadFactor);

                    return new UsdBuySpreadWrapper(response, usdBuySpreadIdr, spreadFactor);
                })
                .cast(Object.class)  
                .onErrorMap(throwable -> new RuntimeException("Failed to fetch or process latest IDR rates", throwable));
    }

    @Override
    public String getResourceKey() {
        return "latest_idr_rates";
    }
}