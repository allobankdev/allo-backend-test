package com.allobank.finance.client;

import com.allobank.finance.dto.LatestRateResponse;
import com.allobank.finance.dto.HistoricalResponse;
import com.allobank.finance.exception.ExternalApiException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

public class FrankfurterClient {

    private final WebClient webClient;

    public FrankfurterClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public LatestRateResponse getLatestIdrRates() {
        try {
            return webClient.get()
                    .uri("/latest?base=IDR")
                    .retrieve()
                    .bodyToMono(LatestRateResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new RuntimeException(
                    "Error External API: " + ex.getStatusCode()
            );
        } catch (Exception ex) {
            throw new ExternalApiException(
                    "Failed to call API: ", 500
            );
        }

    }

    public HistoricalResponse getHistoricalIdrUsd() {
        try {
            return webClient.get()
                    .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                    .retrieve()
                    .bodyToMono(HistoricalResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new RuntimeException(
                    "Error External API: " + ex.getStatusCode()
            );
        } catch (Exception ex) {
            throw new ExternalApiException(
                    "Failed to call API: ", 500
            );
        }

    }

    public Map<String, String> getCurrencies() {
        try {
            return webClient.get()
                    .uri("/currencies")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                    .block();
        } catch (WebClientResponseException ex) {
            throw new RuntimeException(
                    "Error External API: " + ex.getStatusCode()
            );
        } catch (Exception ex) {
            throw new ExternalApiException(
                    "Failed to call API: ", 500
            );
        }

    }
}
