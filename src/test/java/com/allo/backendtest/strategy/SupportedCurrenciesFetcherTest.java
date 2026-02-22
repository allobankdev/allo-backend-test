package com.allo.backendtest.strategy;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SupportedCurrenciesFetcherTest {

    @Test
    void shouldFetchSupportedCurrenciesSuccessfully() {

        String json = """
                {
                  "USD": "United States Dollar"
                }
                """;

        ExchangeFunction exchangeFunction = request ->
                Mono.just(
                        ClientResponse.create(HttpStatus.OK)
                                .header("Content-Type", "application/json")
                                .body(json)
                                .build()
                );

        WebClient webClient = WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();

        SupportedCurrenciesFetcher fetcher =
                new SupportedCurrenciesFetcher(webClient);

        var result = fetcher.fetchAndTransform();

        assertNotNull(result);
        assertFalse(result.isEmpty());

        Map<String, String> first =
                (Map<String, String>) result.get(0);

        assertEquals("USD", first.get("currency"));
        assertEquals("United States Dollar", first.get("description"));
    }
}