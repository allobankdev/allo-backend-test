package com.allobank.finace.service.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("latest_idr_rates")
public class LatestIdrRatesStrategy implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "latest_idr_rates";

    private final double spreadFactor;

    public LatestIdrRatesStrategy(@Value("${spread.github-username}") String githubUsername) {
        this.spreadFactor = calculateSpreadFactor(githubUsername);
        log.info("Spread Factor for username '{}': {}", githubUsername, spreadFactor);
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public List<Map<String, Object>> fetch(WebClient webClient) {
        log.info("Fetching latest IDR rates from Frankfurter API");

        Map<String, Object> response = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (response == null) {
            log.warn("Received null response for latest IDR rates");
            return List.of();
        }

        Map<String, Object> result = new HashMap<>(response);

        @SuppressWarnings("unchecked")
        Map<String, Object> rates = (Map<String, Object>) response.get("rates");

        if (rates != null && rates.containsKey("USD")) {
            double rateUsd = ((Number) rates.get("USD")).doubleValue();
            double usdBuySpreadIdr = (1.0 / rateUsd) * (1.0 + spreadFactor);
            result.put("USD_BuySpread_IDR", usdBuySpreadIdr);
        }

        return List.of(result);
    }

    double calculateSpreadFactor(String username) {
        int sum = username.toLowerCase()
                .chars()
                .sum();
        return (sum % 1000) / 100000.0;
    }
}
