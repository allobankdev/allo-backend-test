package com.allobank.idr.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalIDRUSDStrategy implements IDRDataFetcher {
    
    private final WebClient webClient;

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Map<String, Object> fetchData() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
