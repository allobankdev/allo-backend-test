package com.allo.backend.strategy;

import com.allo.backend.model.HistoricalIdrUsdResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {
    private final WebClient webClient;

    public HistoricalIdrUsdFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Object fetchData() {
        Map<String, Object> response = webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        String base = (String) response.get("base");
        String startDate = "2024-01-01";
        String endDate = "2024-01-05";
        Map<String, Map<String, Double>> rates = (Map<String, Map<String, Double>>) response.get("rates");
        return new HistoricalIdrUsdResponse(startDate, endDate, base, rates);
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }
}
