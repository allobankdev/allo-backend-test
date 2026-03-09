package com.aryaevan.allo.strategy;

import com.aryaevan.allo.dto.HistoricalRatesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Strategy implementation for fetching historical IDR to USD exchange rates.
 * Handles the time series data from Frankfurter API.
 * Queries data from 2024-01-01 to 2024-01-05 with IDR as base and USD as target.
 */
@Component
public class HistoricalIDRUSDStrategy implements IDRDataFetcher {
    
    private final WebClient webClient;
    
    @Autowired
    public HistoricalIDRUSDStrategy(WebClient webClient) {
        this.webClient = webClient;
    }
    
    @Override
    public Object fetchData() {
        // Fetch historical rates for the specified date range
        return webClient
                .get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .block();
    }
    
    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }
}
