package com.allo.test.client;

import com.allo.test.model.api.CurrencyApiResponse;
import com.allo.test.model.api.HistoricalApiResponse;
import com.allo.test.model.api.LatestApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class FrankfurterClient {

    private final WebClient webClient;

    public LatestApiResponse getLatestRates() {
        return webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(LatestApiResponse.class)
                .block();
    }

    public HistoricalApiResponse getHistoricalRates() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalApiResponse.class)
                .block();
    }

    public CurrencyApiResponse getCurrencies() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(CurrencyApiResponse.class)
                .block();
    }
}