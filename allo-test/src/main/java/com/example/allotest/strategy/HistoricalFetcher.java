package com.example.allotest.strategy;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component("historical_idr_usd")
public class HistoricalFetcher implements IDataFetcher {
    private final WebClient webClient;

    public HistoricalFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Object[] fetchData() {
        return new Object[]{webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Object.class)
                .block()};
    }
}
