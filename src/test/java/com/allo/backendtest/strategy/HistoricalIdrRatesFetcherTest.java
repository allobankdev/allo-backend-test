package com.allo.backendtest.strategy;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HistoricalIdrRatesFetcherTest {

    @Test
    void shouldFetchHistoricalRatesSuccessfully() {

        String json = """
                {
                  "base": "IDR",
                  "rates": {
                    "2024-01-01": {
                      "USD": 15000.0
                    }
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
        assertEquals(1, result.size());

        Map<String, Object> first =
                (Map<String, Object>) result.get(0);

        assertEquals("2024-01-01", first.get("date"));
        assertEquals(15000.0, first.get("USD"));
    }
}