package com.allobank.test.service.strategy.impl;

import com.allobank.test.service.DataCacheService;
import com.allobank.test.service.strategy.IDRDataFetcher;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Dibuat oleh: Andre Rizaldi Brillianto
 * Email: andrerizaldib@gmail.com
 * Date: Wednesday, 7-January-2026
 * description: allo-bank-test
 */
@Component("supported_currencies")
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private final WebClient webClient;
    private final DataCacheService dataCacheService;

    public SupportedCurrenciesStrategy(WebClient webClient, DataCacheService dataCacheService) {
        this.webClient = webClient;
        this.dataCacheService = dataCacheService;
    }

    @Override
    public Object fetchData() {
        // Query: /currencies
        JsonNode response = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        // transformasi jadi {"code", "name"}
        List<Map<String, String>> result = new java.util.ArrayList<>();
        if (response != null) {
            response.fields().forEachRemaining(entry -> {
                java.util.Map<String, String> item = new java.util.HashMap<>();
                item.put("code", entry.getKey());
                item.put("name", entry.getValue().asText());
                result.add(item);
            });
        }
        return result;
    }

    // mendapatkan cache data (memory) ketika sudah mendapatkan datanya
    // supaya cepat
    @Override
    public Object getCachedData() {
        return dataCacheService.getData(getResourceType());
    }

    // get resource type
    @Override
    public String getResourceType() {
        return "supported_currencies";
    }
}
