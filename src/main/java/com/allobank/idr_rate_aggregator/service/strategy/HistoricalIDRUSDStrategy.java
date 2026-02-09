package com.allobank.idr_rate_aggregator.service.strategy;


import com.allobank.idr_rate_aggregator.model.ResourceType;
import com.allobank.idr_rate_aggregator.model.dto.HistoricalRatesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HistoricalIDRUSDStrategy implements IDRDataFetcherStrategy {

    private final WebClient webClient;

    public HistoricalIDRUSDStrategy(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Mendukung resource HISTORICAL_IDR_USD
     */
    @Override
    public ResourceType getSupportedType() {
        return ResourceType.HISTORICAL_IDR_USD;
    }

    /**
     * Mengambil historical data IDR ke USD
     */
    @Override
    public Object fetchData() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .block();
    }
}