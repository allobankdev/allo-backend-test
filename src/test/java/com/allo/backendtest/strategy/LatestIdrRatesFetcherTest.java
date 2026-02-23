package com.allo.backendtest.strategy;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        assertEquals(1, result.size());

        Map<String, Object> first =
                (Map<String, Object>) result.get(0);

        assertEquals("USD", first.get("currency"));

        BigDecimal rate = (BigDecimal) first.get("rate");
        assertEquals(BigDecimal.valueOf(15000.0), rate);

        BigDecimal spread =
                (BigDecimal) first.get("USD_BuySpread_IDR");

        assertNotNull(spread);

        // Expected spread:
        // SpreadFactor = 0.00318
        // (1 / 15000) * (1 + 0.00318)

        BigDecimal expected = BigDecimal.ONE
                .divide(BigDecimal.valueOf(15000.0), 8, RoundingMode.HALF_UP)
                .multiply(BigDecimal.ONE.add(
                        BigDecimal.valueOf(0.00318)
                ));

        assertEquals(0, expected.compareTo(spread));
    }
}