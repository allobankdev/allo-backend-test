package com.allobank.backend.strategy;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("supported_currencies")
@RequiredArgsConstructor
public class SupportedCurrencies implements FinanceDataStrategy {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    @Override
    public Mono<JsonNode> fetchAndTransformData() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(String.class)
                .map(jsonString -> {
                    try {
                        return objectMapper.readTree(jsonString);
                    } catch (Exception e) {
                        throw new RuntimeException("Gagal membaca JSON supported currencies", e);
                    }
                });
    }
    
}
