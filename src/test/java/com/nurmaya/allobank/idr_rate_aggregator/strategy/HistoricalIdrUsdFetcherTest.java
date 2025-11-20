package com.nurmaya.allobank.idr_rate_aggregator.strategy;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import com.nurmaya.allobank.idr_rate_aggregator.client.FrankfurterClient;
import com.nurmaya.allobank.idr_rate_aggregator.dto.HistoricalRatesResponse;

import org.junit.jupiter.api.Test;

public class HistoricalIdrUsdFetcherTest {

    @Test
    void testFetchData_ShouldReturnHistoricalRates() {
        FrankfurterClient client = mock(FrankfurterClient.class);

        HistoricalRatesResponse mockResponse = new HistoricalRatesResponse();
        mockResponse.setAmount(1.0);
        mockResponse.setBase("IDR");
        mockResponse.setRates(Map.of(
                "2024-01-01", Map.of("USD", 0.000065)
        ));

        when(client.getHistoricalIdrUsd()).thenReturn(mockResponse);

        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(client);

        List<HistoricalRatesResponse> result = fetcher.fetchData();

        assertEquals(1, result.size());
        assertEquals("IDR", result.get(0).getBase());
        assertEquals(0.000065, result.get(0).getRates().get("2024-01-01").get("USD"));
    }
}
