package com.allobank.allobank_api.strategy;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.allobank.allobank_api.client.frankfurter.FrankfurterClient;
import com.allobank.allobank_api.strategy.impl.HistoricalIdrUsdImpl;

@ExtendWith(MockitoExtension.class)
class HistoricalIdrUsdFetcherTest {

    @Mock
    private FrankfurterClient client;

    @InjectMocks
    private HistoricalIdrUsdImpl fetcher;

    @Test
    void shouldReturnHistoricalData() {
        Map<String, Object> mock = new HashMap<>();
        mock.put("rates", Map.of());

        when(client.getHistoricalRates()).thenReturn(mock);

        var result = fetcher.fetchAndTransform();

        assertNotNull(result);
    }
}