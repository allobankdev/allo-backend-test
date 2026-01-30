package com.example.demo.strategy;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class CurrenciesFetcher implements IDRDataFetcher {

    private final WebClient client;

    public CurrenciesFetcher(WebClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "supported_currencies";
    }

    @Override
    public List<?> fetchData() {

        Map<String, String> response = client.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("Failed to fetch currencies"));

        return List.of(response);
    }
}
