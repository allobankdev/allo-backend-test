package com.example.finance.client;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.finance.exception.ExternalApiException;

@Component
public class FrankfurterClient {

    private final WebClient webClient;

    public FrankfurterClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getLatestRates() {
        return webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .onStatus(
                        HttpStatus::isError,
                        response -> {
                            throw new ExternalApiException("Failed to fetch latest rates");
                        }
                )
                .bodyToMono(String.class)
                .block();
    }

    public String getHistoricalRates() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .onStatus(
                        HttpStatus::isError,
                        response -> {
                            throw new ExternalApiException("Failed to fetch historical rates");
                        }
                )
                .bodyToMono(String.class)
                .block();
    }

    public String getCurrencies() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .onStatus(
                        HttpStatus::isError,
                        response -> {
                            throw new ExternalApiException("Failed to fetch currencies");
                        }
                )
                .bodyToMono(String.class)
                .block();
    }
}