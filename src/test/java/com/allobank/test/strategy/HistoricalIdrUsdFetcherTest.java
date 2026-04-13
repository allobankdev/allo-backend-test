package com.allobank.test.strategy;

import com.allobank.test.client.FrankfurterClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoricalIdrUsdFetcherTest {

    @Mock
    private FrankfurterClient frankfurterClient;

    @InjectMocks
    private HistoricalIdrUsdFetcher fetcher;

    @Test
    void resourceTypeShouldMatchContract() {
        assertEquals("historical_idr_usd", fetcher.resourceType());
    }

    @Test
    void fetchShouldTransformHistoricalRatesIntoUnifiedList() {
        Map<String, Map<String, Object>> rates = new LinkedHashMap<>();
        rates.put("2024-01-02", Map.of("USD", new BigDecimal("0.000064")));
        rates.put("2024-01-01", Map.of("USD", new BigDecimal("0.000063")));

        when(frankfurterClient.fetchHistoricalIdrUsdRaw()).thenReturn(Map.of("rates", rates));

        List<Map<String, Object>> actual = fetcher.fetch();

        assertEquals(2, actual.size());
        assertEquals("historical_idr_usd", actual.get(0).get("resourceType"));
        assertEquals("2024-01-01", actual.get(0).get("date"));
        assertEquals(new BigDecimal("0.000063"), actual.get(0).get("usd_per_idr"));
        assertEquals("2024-01-02", actual.get(1).get("date"));
        assertEquals(new BigDecimal("0.000064"), actual.get(1).get("usd_per_idr"));
    }
}
