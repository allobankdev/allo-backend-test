package com.allo.backendtest.strategy;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LatestIdrRatesFetcherTest {



    @Test
    void shouldCalculateSpreadCorrectly() {

        String json = """
                {
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

        LatestIdrRatesFetcher fetcher =
                new LatestIdrRatesFetcher(webClient, "hollymolly2708");

        var result = fetcher.fetchAndTransform();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}