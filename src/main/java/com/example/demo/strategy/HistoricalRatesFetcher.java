package com.example.demo.strategy;

import com.example.demo.dto.HistoricalResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class HistoricalRatesFetcher implements IDRDataFetcher {

    private final WebClient client;

    public HistoricalRatesFetcher(WebClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "historical_idr_usd";
    }

    @Override
    public List<?> fetchData() {

        HistoricalResponse response = client.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalResponse.class)
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("Failed to fetch historical rates"));

        return List.of(response);
    }
}
