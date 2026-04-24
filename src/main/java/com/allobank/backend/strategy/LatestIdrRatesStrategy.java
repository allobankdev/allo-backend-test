package com.allobank.backend.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;



@Component("latest_idr_rates") 
@RequiredArgsConstructor
public class LatestIdrRatesStrategy implements FinanceDataStrategy {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String GITHUB_USERNAME = "asyifa-zahwa";

    @Override
    public Mono<JsonNode> fetchAndTransformData() {
        return webClient.get()
                .uri("/latest?from=IDR")
                .retrieve()
                .bodyToMono(String.class)
                .map(jsonString -> {
                    try {
                        return objectMapper.readTree(jsonString); 
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to read JSON from Frankfurter", e);
                    }
                })
                .map(this::applyBuySpreadTransformation);
    }

    private JsonNode applyBuySpreadTransformation(JsonNode rootNode) {
        if (rootNode.isObject() && rootNode.has("rates")) {
            ObjectNode rootObject = (ObjectNode) rootNode;
            JsonNode ratesNode = rootObject.get("rates");

            if (ratesNode.has("USD")) {
                double rateUsd = ratesNode.get("USD").asDouble();
                int sum = GITHUB_USERNAME.toLowerCase().chars().sum(); 
                double spreadFactor = (sum % 1000) / 100000.0;
                double usdBuySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);
                rootObject.put("USD_BuySpread_IDR", usdBuySpreadIdr);
            }else {
                throw new IllegalStateException("Calculation failed: Currency USD not found in Frankfurter API response!");
            }
        } else {
             throw new IllegalStateException("Format JSON from Frankfurter is invalid!");
        }
        return rootNode;
    }
}