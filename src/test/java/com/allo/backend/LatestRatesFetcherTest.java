package com.allo.backend;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.allo.backend.strategy.LatestRatesFetcher;

import reactor.core.publisher.Mono;

class LatestRatesFetcherTest {
    private WebClient webClient;
    private LatestRatesFetcher fetcher;

    @BeforeEach
    void setup() {
        webClient = mock(WebClient.class, RETURNS_DEEP_STUBS);
        fetcher = new LatestRatesFetcher(webClient);
    }

    @Test
    void shouldCalculateSpreadCorrectly() {

        Map<String, Object> rates = new HashMap<>();
        rates.put("USD", 0.000065);

        Map<String, Object> response = new HashMap<>();
        response.put("rates", rates);

        when(webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(Map.class))
                .thenReturn(Mono.just(response));

        Object result = fetcher.fetchData();

        assertNotNull(result);

        Map resultMap = (Map) result;

        assertTrue(resultMap.containsKey("USD_BuySpread_IDR"));
    }
}
