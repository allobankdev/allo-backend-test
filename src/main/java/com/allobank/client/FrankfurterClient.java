package com.allobank.client;

import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class FrankfurterClient {

    private static final String BASE_URL = "https://api.frankfurter.app";

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getLatestRates() {
        String url = BASE_URL + "/latest?from=IDR";
        return restTemplate.getForObject(url, Map.class);
    }

    public Map<String, Object> getHistoricalRates() {
        String url = BASE_URL + "/2024-01-01..2024-01-05?from=IDR&to=USD";
        return restTemplate.getForObject(url, Map.class);
    }

    public Map<String, String> getSupportedCurrencies() {
        String url = BASE_URL + "/currencies";
        return restTemplate.getForObject(url, Map.class);
    }
}