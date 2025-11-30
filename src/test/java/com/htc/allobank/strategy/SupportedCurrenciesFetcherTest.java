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

public class SupportedCurrenciesFetcherTest {

    SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setUp() {

        String fakeJson = """
        {
          "USD": "United States Dollar",
          "IDR": "Indonesian Rupiah",
          "EUR": "Euro"
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

        fetcher = new SupportedCurrenciesFetcher(fakeClient);
    }

    @Test
    void fetch_returnsCurrencyList() {

        Map result = (Map) fetcher.fetch().block();

        assertNotNull(result);

        assertEquals("United States Dollar", result.get("USD"));
        assertEquals("Indonesian Rupiah", result.get("IDR"));
        assertEquals("Euro", result.get("EUR"));
    }
}
