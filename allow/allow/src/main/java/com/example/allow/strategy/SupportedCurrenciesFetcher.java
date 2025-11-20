package com.example.allow.strategy;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public SupportedCurrenciesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Object> fetchData() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorMap(e -> new RuntimeException("Failed to fetch currencies", e));
    }

    @Override
    public String getResourceKey() {
        return "supported_currencies";
    }
}
