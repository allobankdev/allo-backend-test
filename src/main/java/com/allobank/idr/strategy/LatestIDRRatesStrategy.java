package com.allobank.idr.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component("latest_idr_rates")
@RequiredArgsConstructor
public class LatestIDRRatesStrategy implements IDRDataFetcher {
    
    private final WebClient webClient;
    
    @Value("${github.username}")
    private String githubUsername;

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Map<String, Object> fetchData() {
        Map<String, Object> response = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response != null && response.containsKey("rates")) {
            Map<String, Object> rates = (Map<String, Object>) response.get("rates");
            if (rates.containsKey("USD")) {
                double usdRate = ((Number) rates.get("USD")).doubleValue();
                double spreadFactor = calculateSpreadFactor(githubUsername);
                double usdBuySpreadIDR = (1 / usdRate) * (1 + spreadFactor);
                
                Map<String, Object> result = new HashMap<>(response);
                result.put("USD_BuySpread_IDR", usdBuySpreadIDR);
                result.put("spread_factor", spreadFactor);
                return result;
            }
        }
        return response;
    }

    private double calculateSpreadFactor(String username) {
        int sum = username.toLowerCase().chars().sum();
        return (sum % 1000) / 100000.0;
    }
}
