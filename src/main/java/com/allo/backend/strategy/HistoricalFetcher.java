package com.allo.backend.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HistoricalFetcher implements IDRDataFetcher {
    private final WebClient webClient;

    public HistoricalFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetchData() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

}
