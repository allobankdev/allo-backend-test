package com.allo.backend.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CurrenciesFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public CurrenciesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getType() {
        return "supported_currencies";
    }

    @Override
    public Object fetchData() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

}
