package com.example.finance.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class FinanceService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://api.frankfurter.app";

    public List<Object> getFinanceData(String resourceType) {

        switch (resourceType) {

            case "latest_idr_rates":
                return List.of(fetchLatestIdrRates());

            case "historical_idr_usd":
                return List.of(fetchHistoricalIdrUsd());

            case "supported_currencies":
                return List.of(fetchSupportedCurrencies());

            default:
                throw new IllegalArgumentException("Invalid resourceType");
        }
    }

    private Map<String, Object> fetchLatestIdrRates() {
        Map response = restTemplate.getForObject(
                BASE_URL + "/latest?base=IDR", Map.class);

        Map<String, Double> rates =
                (Map<String, Double>) response.get("rates");

        double rateUsd = rates.get("USD");
        double spreadFactor = calculateSpreadFactor("joasputrasaragih");

        double usdBuySpreadIdr =
                (1 / rateUsd) * (1 + spreadFactor);

        response.put("USD_BuySpread_IDR", usdBuySpreadIdr);
        response.put("spreadFactor", spreadFactor);

        return response;
    }

    private Object fetchHistoricalIdrUsd() {
        return restTemplate.getForObject(
                BASE_URL + "/2024-01-01..2024-01-05?from=IDR&to=USD",
                Object.class
        );
    }

    private Object fetchSupportedCurrencies() {
        return restTemplate.getForObject(
                BASE_URL + "/currencies",
                Object.class
        );
    }

    private double calculateSpreadFactor(String githubUsername) {
        int sum = githubUsername
                .toLowerCase()
                .chars()
                .sum();

        return (sum % 1000) / 100000.0;
    }
}
