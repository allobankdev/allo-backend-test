package com.allobank.allobank_api.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.allobank.allobank_api.client.frankfurter.FrankfurterClient;
import com.allobank.allobank_api.strategy.impl.LatestIdrRatesImpl;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesFetcherTest {

    @Mock
    private FrankfurterClient client;

    @InjectMocks
    private LatestIdrRatesImpl fetcher;

    @Test
    void shouldCalculateUsdSpreadCorrectly() {
        Map<String, Object> mockResponse = new HashMap<>();

        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 0.000064);

        mockResponse.put("base", "IDR");
        mockResponse.put("rates", rates);

        when(client.getLatestRates()).thenReturn(mockResponse);

        var result = fetcher.fetchAndTransform();

        assertNotNull(result);
        assertEquals("IDR", result.getBase());
        assertNotNull(result.getUsdBuySpreadIdr());
    }
}
