package com.allobank.finance.client;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FrankfurterClient {

    private final WebClient webClient;

    public FrankfurterClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Map<String, Object> getLatestIdrRates() {
        return webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> getHistoricalIdrUsd() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> getSupportedCurrencies() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
