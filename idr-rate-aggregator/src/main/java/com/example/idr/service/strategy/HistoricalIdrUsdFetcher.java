package com.example.idr.service.strategy;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.idr.client.dto.HistoricalRatesResponse;
import com.example.idr.model.HistoricalRateResult;

@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public HistoricalIdrUsdFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public List<?> fetchAndTransform() {
        HistoricalRatesResponse response = webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .block();

        return response.getRates().entrySet().stream()
                .map(e ->
                        new HistoricalRateResult(
                                e.getKey(),
                                e.getValue().get("USD")
                        )
                )
                .toList();
    }
}
