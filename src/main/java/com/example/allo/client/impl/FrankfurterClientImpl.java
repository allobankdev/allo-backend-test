package com.example.allo.client.impl;

import com.example.allo.client.FrankfurterClient;
import com.example.allo.dto.CurrenciesResponse;
import com.example.allo.dto.HistoricalRatesResponse;
import com.example.allo.dto.LatestRatesResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

public class FrankfurterClientImpl implements FrankfurterClient {
    private final WebClient webClient;

    public FrankfurterClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public LatestRatesResponse getLatestRates(String base) {
        return webClient.get()
                .uri("/latest?base={base}", base)
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .block();
    }

    @Override
    public HistoricalRatesResponse getHistoricalRates(
            String start, String end, String from, String to) {

        return webClient.get()
                .uri("/{start}..{end}?from={from}&to={to}",
                        start, end, from, to)
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .block();
    }

    @Override
    public Map<String, String> getCurrencies() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();
    }
}
