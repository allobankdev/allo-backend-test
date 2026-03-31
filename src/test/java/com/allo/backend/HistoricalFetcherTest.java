package com.allo.backend;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.allo.backend.strategy.HistoricalFetcher;

import reactor.core.publisher.Mono;

class HistoricalFetcherTest {
    private WebClient webClient;
    private HistoricalFetcher fetcher;

    @BeforeEach
    void setup() {
        webClient = mock(WebClient.class, RETURNS_DEEP_STUBS);
        fetcher = new HistoricalFetcher(webClient);
    }

    @Test
    void shouldFetchHistoricalData() {

        when(webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Map.class))
                .thenReturn(Mono.just(Map.of("test", "data")));

        Object result = fetcher.fetchData();

        assertNotNull(result);
    }
}
