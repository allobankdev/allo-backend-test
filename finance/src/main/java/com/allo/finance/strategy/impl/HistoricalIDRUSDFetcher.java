package com.allo.finance.strategy.impl;

import com.allo.finance.dto.HistoricalRatesResponse;
import com.allo.finance.strategy.IDRDataFetcher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HistoricalIDRUSDFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public HistoricalIDRUSDFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetchData() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .block();
    }
}
