package com.allo_backend_test.client;

import com.allo_backend_test.dto.HistoricalRatesResponse;
import com.allo_backend_test.dto.LatestRatesResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class FrankfurterApiClient {

    private final WebClient webClient;

    public FrankfurterApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public LatestRatesResponse fetchLatestIdrRates() {
        return webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .block();
    }

    public HistoricalRatesResponse fetchHistoricalIdrUsd() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .block();
    }

    public Map<String, String> fetchSupportedCurrencies() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();
    }

}
