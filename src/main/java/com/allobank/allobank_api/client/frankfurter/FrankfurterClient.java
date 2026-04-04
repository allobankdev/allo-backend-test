package com.allobank.allobank_api.client.frankfurter;


import java.util.Map;

import org.springframework.web.reactive.function.client.WebClient;

public class FrankfurterClient {

    private final WebClient webClient;

    public FrankfurterClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Map<String, Object> getLatestRates() {
        return webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> getHistoricalRates() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, String> getCurrencies() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}