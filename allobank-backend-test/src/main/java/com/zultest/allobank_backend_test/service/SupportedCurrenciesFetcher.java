package com.zultest.allobank_backend_test.service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcherInterface{
    private final WebClient webClient;

    public SupportedCurrenciesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String resourceType() {
        return "supported_currencies";
    }

    @Override
    public List<?> fetchData() {
        Map<String, Object> response = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return List.of(response);
    }
}
