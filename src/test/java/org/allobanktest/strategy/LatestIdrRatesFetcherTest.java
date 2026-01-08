package org.allobanktest.strategy;

import org.allobanktest.dto.LatestRatesItem;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LatestIdrRatesFetcherTest {
    @Test
    void testSpreadFactorCalculationAndTransformation() {
        String json = """
                {
                  "amount": 1.0,
                  "base": "IDR",
                  "date": "2026-01-06",
                  "rates": {
                    "USD": 0.00006,
                    "EUR": 0.000051
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

        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher();

        List<?> result = fetcher.load(client, "bluntswordman");

        assertFalse(result.isEmpty());
        LatestRatesItem usdItem = (LatestRatesItem) result.stream()
                .filter(i -> ((LatestRatesItem) i).currency().equals("USD"))
                .findFirst()
                .orElseThrow();

        assertEquals("IDR", usdItem.base());
        assertTrue(usdItem.usdBuySpreadIdr() > 0.0, "Spread harus dihitung");
    }
}
