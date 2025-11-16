package com.allobank.allobackendtest.strategy;

import com.allobank.allobackendtest.dto.HistoricalRatesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class HistoricalIdrUsdFetcher implements IdrDataFetcher {

    private final WebClient webClient;

    public HistoricalIdrUsdFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String resourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetchFromApi() {
        // Range sesuai pertanyaan: 2024-01-01..2024-01-05
        record RawHistorical(Map<String, Map<String, BigDecimal>> rates) {}

        RawHistorical raw = webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(RawHistorical.class)
                .block();

        return new HistoricalRatesResponse(
                resourceType(),
                raw != null ? raw.rates() : Map.of()
        );
    }
}
