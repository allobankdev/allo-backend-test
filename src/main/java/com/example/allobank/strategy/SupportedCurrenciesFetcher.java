package com.example.allobank.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component("supported_currencies")
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final WebClient client;

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public List<?> fetchData() {
        Map<String, String> response = client.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

        return response.entrySet()
                .stream()
                .map(e -> Map.of("code", e.getKey(), "name", e.getValue()))
                .toList();
    }
}