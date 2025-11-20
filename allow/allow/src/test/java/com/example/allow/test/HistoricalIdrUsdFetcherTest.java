package com.example.allow.test;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.allow.strategy.HistoricalIdrUsdFetcher;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HistoricalIdrUsdFetcherTest {

    @Test
    void shouldReturnHistoricalData() {
        WebClient webClient = mock(WebClient.class);
        var uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        var headersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(any(String.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class))
                .thenReturn(Mono.just(Map.of("base", "IDR", "rates", Map.of("2024-01-02", Map.of("USD", 6.4E-5)))));

        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(webClient);

        StepVerifier.create(fetcher.fetchData())
                .expectNextMatches(data -> data instanceof Map)
                .verifyComplete();
    }
}