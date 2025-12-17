package com.allo.finance.strategy;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HistoricalIdrUsdStrategyTest {

    @Test
    void shouldFetchHistoricalData() {
        WebClient client = Mockito.mock(WebClient.class, Mockito.RETURNS_DEEP_STUBS);

        Mockito.when(
                client.get()
                        .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block()
        ).thenReturn(Map.of("rates", Map.of()));

        HistoricalIdrUsdStrategy strategy = new HistoricalIdrUsdStrategy(client);

        Object result = strategy.fetch();

        assertNotNull(result);
    }
}
