package org.allobanktest.strategy;

import org.allobanktest.dto.HistoricalUsdItem;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HistoricalIdrUsdFetcherTest {
    @Test
    void testHistoricalTransformation() {
        String json = """
                {
                  "amount": 1.0,
                  "base": "IDR",
                  "start_date": "2024-12-31",
                  "end_date": "2025-01-03",
                  "rates": {
                    "2024-12-31": { "USD": 0.000062 },
                    "2025-01-02": { "USD": 0.000063 }
                  }
                }
                """;

        WebClient client = WebClient.builder()
                .exchangeFunction(request -> {
                    ClientResponse response = ClientResponse.create(HttpStatus.OK)
                            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .body(json)
                            .build();
                    return Mono.just(response);
                })
                .build();

        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher();

        List<?> result = fetcher.load(client, "bluntswordman");

        assertEquals(2, result.size());
        HistoricalUsdItem item = (HistoricalUsdItem) result.get(0);
        assertEquals("2024-12-31", item.date());
        assertTrue(item.usdRate() > 0);
    }
}
