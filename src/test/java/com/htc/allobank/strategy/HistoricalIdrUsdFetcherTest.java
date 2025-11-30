package com.htc.allobank.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HistoricalIdrUsdFetcherTest {

    HistoricalUsdFetcher fetcher;

    @BeforeEach
    void setUp() {

        String fakeJson = """
        {
          "rates": {
            "2024-01-01": { "USD": 0.000067 },
            "2024-01-02": { "USD": 0.000068 }
          }
        }
        """;

        ExchangeFunction fakeExchange = request ->
          Mono.just(
            ClientResponse.create(HttpStatusCode.valueOf(200))
              .header("Content-Type","application/json")
              .body(fakeJson)
              .build()
          );

        WebClient fakeClient = WebClient.builder()
          .exchangeFunction(fakeExchange)
          .build();

        fetcher = new HistoricalUsdFetcher(fakeClient);
    }

    @Test
    void fetch_returnsHistoricalRates() {

        Map result = (Map) fetcher.fetch().block();

        assertNotNull(result);
        assertTrue(result.containsKey("rates"));

        Map rates = (Map) result.get("rates");

        assertTrue(rates.containsKey("2024-01-01"));
        assertTrue(rates.containsKey("2024-01-02"));
    }
}
