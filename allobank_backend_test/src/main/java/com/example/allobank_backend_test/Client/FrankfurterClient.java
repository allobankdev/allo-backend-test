package com.example.allobank_backend_test.Client;

import com.example.allobank_backend_test.DTO.LatestRatesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class FrankfurterClient {

    private final WebClient webClient;

    public LatestRatesResponse getLatestRates() {
        return webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .block();
    }

    public Object getHistorical() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    public Object getCurrencies() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}
