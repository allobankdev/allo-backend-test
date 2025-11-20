package com.example.allow.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public HistoricalIdrUsdFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Object> fetchData() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Object.class)
                .map(response -> {
                    if (response instanceof Map<?, ?> map) {
                        Object rates = map.get("rates");
                        if (rates instanceof Map<?, ?> ratesMap) {
                            ratesMap.keySet().removeIf(key -> key.toString().startsWith("2023"));
                        }
                    }
                    return response;
                });
    }

    @Override
    public String getResourceKey() {
        return "historical_idr_usd";
    }
}
