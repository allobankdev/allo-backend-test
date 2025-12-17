package com.allo.finance.strategy;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private final WebClient client;

    public SupportedCurrenciesStrategy(WebClient client) {
        this.client = client;
    }

    @Override
    public String resourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetch() {
        return client.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
