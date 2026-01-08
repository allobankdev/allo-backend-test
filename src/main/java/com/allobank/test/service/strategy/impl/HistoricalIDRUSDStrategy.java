package com.allobank.test.service.strategy.impl;

import com.allobank.test.service.strategy.IDRDataFetcher;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Dibuat oleh: Andre Rizaldi Brillianto
 * Email: andrerizaldib@gmail.com
 * Date: Wednesday, 7-January-2026
 * description: allo-bank-test
 */
@Component("historical_idr_usd")
public class HistoricalIDRUSDStrategy implements IDRDataFetcher {

    private final WebClient webClient;
    private final com.allobank.test.service.DataCacheService dataCacheService;

    public HistoricalIDRUSDStrategy(WebClient webClient, com.allobank.test.service.DataCacheService dataCacheService) {
        this.webClient = webClient;
        this.dataCacheService = dataCacheService;
    }

    @Override
    public Object fetchData() {
        // Query: mencari data dari = 2024-01-01 sampai 2024-01-05?from=IDR&to=USD
        JsonNode response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/2024-01-01..2024-01-05")
                        .queryParam("from", "IDR")
                        .queryParam("to", "USD")
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        // Transform into unified JSON array: [{"date": "...", "rate": ...}, ...]
        // transform ke JSON array = {"date":, "rate":}
        java.util.List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
        if (response != null && response.has("rates")) {
            response.get("rates").fields().forEachRemaining(entry -> {
                java.util.Map<String, Object> item = new java.util.HashMap<>();
                item.put("date", entry.getKey());
                if (entry.getValue().has("USD")) {
                    item.put("rate", entry.getValue().get("USD").decimalValue());
                }
                result.add(item);
            });
        }
        return result;
    }

    // ketika sudah ada datanya tinggal panggil di cache(memory)
    @Override
    public Object getCachedData() {
        return dataCacheService.getData(getResourceType());
    }

    // resource type
    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }
}
