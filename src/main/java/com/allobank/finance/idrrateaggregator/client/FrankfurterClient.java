package com.allobank.finance.idrrateaggregator.client;

import com.allobank.finance.idrrateaggregator.model.dto.HistoricalRatesResponse;
import com.allobank.finance.idrrateaggregator.model.dto.LatestRatesResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class FrankfurterClient {

    private final WebClient webClient;

    public FrankfurterClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public LatestRatesResponse getLatestIdrRates() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("base", "IDR")
                        .build())
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .block();
    }

    public HistoricalRatesResponse getHistoricalIdrUsdRates() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/2024-01-01..2024-01-05")
                        .queryParam("from", "IDR")
                        .queryParam("to", "USD")
                        .build())
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .block();
    }

    public Map<String, String> getSupportedCurrencies() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();
    }
}
