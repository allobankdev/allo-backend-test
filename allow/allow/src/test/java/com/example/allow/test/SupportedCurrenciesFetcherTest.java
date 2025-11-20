package com.example.allow.test;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.allow.strategy.SupportedCurrenciesFetcher;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Map;

class SupportedCurrenciesFetcherTest {

    @Test
    void shouldReturnCurrencyList() {
        WebClient webClient = mock(WebClient.class);
        var uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        var headersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri("/currencies")).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class))
                .thenReturn(Mono.just(Map.of("USD", "United States Dollar", "EUR", "Euro")));

        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(webClient);

        StepVerifier.create(fetcher.fetchData())
                .expectNextMatches(data -> data instanceof Map<?, ?> map && map.containsKey("USD"))
                .verifyComplete();
    }
}
