package com.allobank.finance.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.allobank.finance.client.FrankfurterClient;

class HistoricalIdrUsdStrategyTest {

    @Mock
    private FrankfurterClient client;

    private HistoricalIdrUsdStrategy strategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        strategy = new HistoricalIdrUsdStrategy(client);
    }

    @Test
    void fetch_shouldReturnHistoricalIdrUsdData() {
        // given
        Map<String, Object> mockResponse = Map.of(
                "base", "IDR",
                "rates", Map.of("2023-01-01", Map.of("USD", 0.000065))
        );

        when(client.getHistoricalIdrUsd()).thenReturn(mockResponse);

        // when
        Object result = strategy.fetch();

        // then
        assertEquals(mockResponse, result);
        verify(client).getHistoricalIdrUsd();
    }

    @Test
    void getResourceType_shouldReturnHistoricalIdrUsd() {
        assertEquals("historical_idr_usd", strategy.getResourceType());
    }
}
