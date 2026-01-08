package com.allobank.test.service.strategy.impl;

import com.allobank.test.service.DataCacheService;
import com.allobank.test.service.strategy.IDRDataFetcher;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Dibuat oleh: Andre Rizaldi Brillianto
 * Email: andrerizaldib@gmail.com
 * Date: Wednesday, 7-January-2026
 * description: allo-bank-test
 */
@Component("latest_idr_rates")
public class LatestIDRRatesStrategy implements IDRDataFetcher {

    private final WebClient webClient;
    private final DataCacheService dataCacheService;

    public LatestIDRRatesStrategy(WebClient webClient, DataCacheService dataCacheService) {
        this.webClient = webClient;
        this.dataCacheService = dataCacheService;
    }

    private static final String GITHUB_USERNAME = "codelamps-academy";

    @Override
    public Object fetchData() {
        JsonNode response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("base", "IDR")
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return transformData(response);
    }

    @Override
    public Object getCachedData() {
        return dataCacheService.getData(getResourceType());
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    private Map<String, Object> transformData(JsonNode rootNode) {
        Map<String, Object> result = new HashMap<>();

        // copy di map baru, supaya bisa menambahkan field USD_BuySpread_IDR ke rootNode
        // karena JsonNode bersifat immutable/baca saja
        if (rootNode.has("amount"))
            result.put("amount", rootNode.get("amount").asDouble());
        if (rootNode.has("base"))
            result.put("base", rootNode.get("base").asText());
        if (rootNode.has("date"))
            result.put("date", rootNode.get("date").asText());

        // dirubah jadi BigDecimal
        Map<String, BigDecimal> rates = new java.util.HashMap<>();
        if (rootNode != null && rootNode.has("rates")) {
            rootNode.get("rates").fields().forEachRemaining(entry -> {
                rates.put(entry.getKey(), entry.getValue().decimalValue());
            });
        }
        result.put("rates", rates);

        // menghitung USD_BuySpread_IDR dari username github
        if (rates.containsKey("USD")) {
            BigDecimal rateUsd = rates.get("USD");
            double spreadFactor = calculateSpreadFactor(GITHUB_USERNAME);
            // Rumus: USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
            double usdBuySpreadIdr = (1.0 / rateUsd.doubleValue()) * (1.0 + spreadFactor);
            result.put("USD_BuySpread_IDR", usdBuySpreadIdr);
        }

        return result;
    }

    // perhitungan untuk username
    private double calculateSpreadFactor(String username) {
        int sum = 0;
        for (char c : username.toCharArray()) {
            sum += c;
        }
        // Spread Factor = (Sum of Unicode Values % 1000) / 100000.0
        return (sum % 1000) / 100000.0;
    }
}
