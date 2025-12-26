package com.allobank.finance.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.allobank.finance.dto.CurrencyResponse;
import com.allobank.finance.dto.HistoricalRateResponse;
import com.allobank.finance.dto.LatestRateResponse;



@Component
public class FrankfurterClient {

    private final WebClient webClient;

    public FrankfurterClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public LatestRateResponse getLatestRates() {
        return webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(LatestRateResponse.class)
                .block();
    }

    public HistoricalRateResponse getHistoricalRates() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalRateResponse.class)
                .block();
    }

    public CurrencyResponse getCurrencies() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(CurrencyResponse.class)
                .block();
    }
}

