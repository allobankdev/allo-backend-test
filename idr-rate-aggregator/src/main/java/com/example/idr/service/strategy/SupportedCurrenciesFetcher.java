package com.example.idr.service.strategy;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.idr.model.CurrencyResult;

@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public SupportedCurrenciesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public List<?> fetchAndTransform() {
        Map<String, String> response = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(
                        new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

        return response.entrySet().stream()
                .map(e -> new CurrencyResult(e.getKey(), e.getValue()))
                .toList();
    }
}
