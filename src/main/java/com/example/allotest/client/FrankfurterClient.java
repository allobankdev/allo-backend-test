package com.example.allotest.client;

import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.util.retry.Retry;

@Component
public class FrankfurterClient {
    private final WebClient webClient;

    public FrankfurterClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Object getLatest() {
        return fetch("/latest?base=IDR");
    }

    public Object getHistorical() {
        return fetch("/2026-01-01..2026-01-05?from=IDR&to=USD");
    }

    public Object getSupportedCurrencies() {
        return fetch("/currencies");
    }

    private Object fetch(String uri) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Object.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .block();
    }
}