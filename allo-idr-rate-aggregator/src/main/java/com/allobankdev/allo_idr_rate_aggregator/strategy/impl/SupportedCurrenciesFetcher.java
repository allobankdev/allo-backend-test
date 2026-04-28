package com.allobankdev.allo_idr_rate_aggregator.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.allobankdev.allo_idr_rate_aggregator.strategy.IDRDataFetcher;
import com.allobankdev.allo_idr_rate_aggregator.model.dto.CurrencyResponse;

@Component
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public String resourceType() {
        return "supported_currencies";
    }

    @Override
    public Mono<List<Object>> fetch() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, String>>() {})
                .log()
                .map(currencyMap ->
                        currencyMap.entrySet()
                                .stream()
                                .map(e -> new CurrencyResponse(e.getKey(), e.getValue()))
                                .collect(Collectors.toList())
                );
                // .map(list -> (List<Object>) (List<?>) list);
    }
}

