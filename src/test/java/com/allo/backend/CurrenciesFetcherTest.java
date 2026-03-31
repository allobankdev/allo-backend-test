package com.allo.backend;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.allo.backend.strategy.CurrenciesFetcher;

import reactor.core.publisher.Mono;

class CurrenciesFetcherTest {
    private WebClient webClient;
    private CurrenciesFetcher fetcher;

    @BeforeEach
    void setup() {
        webClient = mock(WebClient.class, RETURNS_DEEP_STUBS);
        fetcher = new CurrenciesFetcher(webClient);
    }

    @Test
    void shouldFetchCurrencies() {

        when(webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class))
                .thenReturn(Mono.just(Map.of("USD", "Dollar")));

        Object result = fetcher.fetchData();

        assertNotNull(result);
    }
}
