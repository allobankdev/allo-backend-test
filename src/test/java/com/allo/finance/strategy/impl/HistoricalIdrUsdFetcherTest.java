package com.allo.finance.strategy.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HistoricalIdrUsdFetcherTest {

    @Test
    void shouldReturnHistoricalData() {
        WebClient webClient = Mockito.mock(WebClient.class, Mockito.RETURNS_DEEP_STUBS);

        Map<String, Object> mockResponse = Map.of(
                "rates", Map.of("2024-01-01", Map.of("USD", 0.000064))
        );

        Mockito.when(
                webClient.get()
                        .uri(Mockito.anyString())
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block()
        ).thenReturn(mockResponse);

        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(webClient);

        Object result = fetcher.fetchData();

        assertThat(result).isNotNull();
    }

}