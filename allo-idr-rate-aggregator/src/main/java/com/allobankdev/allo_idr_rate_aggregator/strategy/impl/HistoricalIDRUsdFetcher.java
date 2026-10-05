package com.allobankdev.allo_idr_rate_aggregator.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.allobankdev.allo_idr_rate_aggregator.strategy.IDRDataFetcher;

import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HistoricalIDRUsdFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public String resourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Mono<List<Object>> fetch() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Map.class)
                .log()
                .map(Collections::singletonList);
    }
}

