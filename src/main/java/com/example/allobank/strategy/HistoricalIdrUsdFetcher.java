package com.example.allobank.strategy;

import com.example.allobank.dto.HistoricalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final WebClient client;

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public List<?> fetchData() {
        HistoricalResponse response = client.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalResponse.class)
                .block();

        return List.of(response);
    }
}