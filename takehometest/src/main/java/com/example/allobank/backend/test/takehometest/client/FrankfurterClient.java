package com.example.allobank.backend.test.takehometest.client;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class FrankfurterClient {
    private final WebClient webClient;

    public FrankfurterClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Map<String, Object> getLatestIdrRates() {
        return webClient
                .get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }

    public Map<String, Object> getHistoricalIdrUsd(String start, String end, String from, String to) {
        return webClient
                .get()
                .uri("/{start}..{end}?from={from}&to={to}",
                        start, end, from, to)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }

    public Map<String, Object> getSupporCurrencies() {
        return webClient
                .get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }
}
