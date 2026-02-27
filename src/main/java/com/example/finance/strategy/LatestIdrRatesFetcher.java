package com.example.finance.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("latest_idr_rates")
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final RestTemplate restTemplate;
    private final String githubUsername;

    @Autowired
    public LatestIdrRatesFetcher(RestTemplate restTemplate,
                                 @org.springframework.beans.factory.annotation.Value("${app.github.username:unknown}") String githubUsername) {
        this.restTemplate = restTemplate;
        this.githubUsername = githubUsername;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public List<Map<String, Object>> fetchData() {
        String url = "https://api.frankfurter.app/latest?base=IDR";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Object ratesObj = response.get("rates");
        List<Map<String, Object>> result = new ArrayList<>();
        if (ratesObj instanceof Map) {
            Map<String, Double> rates = (Map<String, Double>) ratesObj;
            double rateUsd = rates.getOrDefault("USD", 0.0);
            double spreadFactor = calculateSpreadFactor();
            double usdBuySpread = (1 / rateUsd) * (1 + spreadFactor);
            for (Map.Entry<String, Double> entry : rates.entrySet()) {
                Map<String, Object> map = Map.of(
                        "currency", entry.getKey(),
                        "rate", entry.getValue(),
                        "USD_BuySpread_IDR", usdBuySpread
                );
                result.add(map);
            }
        }
        return result;
    }

    private double calculateSpreadFactor() {
        String username = githubUsername == null ? "" : githubUsername.toLowerCase();
        int sum = 0;
        for (char c : username.toCharArray()) {
            sum += c;
        }
        return (sum % 1000) / 100000.0;
    }
}