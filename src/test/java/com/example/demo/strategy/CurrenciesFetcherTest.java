package com.example.demo.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CurrenciesFetcherTest {

    private WebClient webClient;
    private WebClient.RequestHeadersUriSpec uriSpec;
    private WebClient.RequestHeadersSpec headersSpec;
    private WebClient.ResponseSpec responseSpec;

    private CurrenciesFetcher fetcher;

    @BeforeEach
    void setup() {
        webClient = mock(WebClient.class);
        uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        headersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        fetcher = new CurrenciesFetcher(webClient);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void shouldFetchCurrencies() {

        Map<String, String> fakeResponse = Map.of(
                "USD", "Dollar",
                "EUR", "Euro"
        );

        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.just(fakeResponse));

        List<?> result = fetcher.fetchData();

        assertEquals(1, result.size());
        assertEquals(fakeResponse, result.get(0));
    }

}
