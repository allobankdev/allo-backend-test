package com.allo.backendtest.strategy;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class HistoricalIdrRatesFetcherTest {

    @Test
    void shouldFetchHistoricalRatesSuccessfully() {

        String json = """
                {
                  "amount": 1.0,
                  "base": "IDR",
                  "date": "2024-01-01",
                  "rates": {
                    "USD": 15000.0
                  }
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

        HistoricalIdrUsdFetcher fetcher =
                new HistoricalIdrUsdFetcher(webClient);

        var result = fetcher.fetchAndTransform();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}