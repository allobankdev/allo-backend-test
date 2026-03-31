package com.allobank.test.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class LatestIdrRatesStrategy implements DataFetcherStrategy {

    private final WebClient webClient;

    @Value("${app.github.username:tech-enthusiast-168}")
    private String githubUsername;

    @Autowired
    public LatestIdrRatesStrategy(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchAndTransform() {
        Map<String, Object> response = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (response != null && response.containsKey("rates")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> rates = (Map<String, Object>) response.get("rates");
            
            if (rates.containsKey("USD")) {
                double rateUsd = Double.parseDouble(String.valueOf(rates.get("USD")));
                double spreadFactor = calculateSpreadFactor();
                double usdBuySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);
                rates.put("USD_BuySpread_IDR", usdBuySpreadIdr);
            }
        }
        return response;
    }

    private double calculateSpreadFactor() {
        String username = githubUsername.toLowerCase();
        int sum = 0;
        for (char c : username.toCharArray()) {
            sum += c;
        }
        return (sum % 1000) / 100000.0;
    }
}
