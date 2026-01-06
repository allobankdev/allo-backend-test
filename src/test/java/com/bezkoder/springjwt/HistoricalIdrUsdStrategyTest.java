package com.bezkoder.springjwt;

import com.bezkoder.springjwt.client.FrankfurterApiClient;
import com.bezkoder.springjwt.store.FinanceDataStore;
import com.bezkoder.springjwt.strategy.HistoricalIdrUsdStrategy;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HistoricalIdrUsdStrategyTest {

    @Test
    void shouldTransformHistoricalRatesToArray() {
        FrankfurterApiClient api = mock(FrankfurterApiClient.class);
        FinanceDataStore store = new FinanceDataStore();

        when(api.getHistoricalIdrToUsd()).thenReturn(Map.of(
                "base", "IDR",
                "rates", Map.of(
                        "2024-01-01", Map.of("USD", 0.000064),
                        "2024-01-02", Map.of("USD", 0.000063)
                )
        ));

        HistoricalIdrUsdStrategy s = new HistoricalIdrUsdStrategy(api, store);
        s.loadAtStartup();

        assertEquals(2, s.loadedData().size());
        Map<String, Object> first = (Map<String, Object>) s.loadedData().get(0);
        assertTrue(first.containsKey("date"));
        assertEquals("IDR", first.get("base"));
        assertTrue(first.containsKey("rates"));
    }
}
