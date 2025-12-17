package com.allo.finance.strategy;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

public class HistoricalIdrUsdStrategy implements IDRDataFetcher {

    private final WebClient client;

    public HistoricalIdrUsdStrategy(WebClient client) {
        this.client = client;
    }

    @Override
    public String resourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetch() {
        return client.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
