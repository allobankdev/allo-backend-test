package com.allo.finance.strategy.impl;

import com.allo.finance.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final WebClient frankfurterWebClient;

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetchData() {
        return frankfurterWebClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

}