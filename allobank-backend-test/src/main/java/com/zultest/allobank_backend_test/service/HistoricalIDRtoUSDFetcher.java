package com.zultest.allobank_backend_test.service;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

public class HistoricalIDRtoUSDFetcher implements IDRDataFetcherInterface {
    private final WebClient webClient;

    public HistoricalIDRtoUSDFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String resourceType() {
        return "historical_idr_usd";
    }

    @Override
    public List<?> fetchData() {
        Map<String, Object> response = webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return List.of(response);
    }
}
