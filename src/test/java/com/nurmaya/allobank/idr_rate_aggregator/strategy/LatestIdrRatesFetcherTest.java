package com.nurmaya.allobank.idr_rate_aggregator.strategy;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.List;

import com.nurmaya.allobank.idr_rate_aggregator.client.FrankfurterClient;
import com.nurmaya.allobank.idr_rate_aggregator.dto.LatestRatesResponse;
import com.nurmaya.allobank.idr_rate_aggregator.util.SpreadFactorCalculator;

import org.junit.jupiter.api.Test;

public class LatestIdrRatesFetcherTest {

    @Test
    void testFetchData_ShouldCalculateSpreadCorrectly() {
        FrankfurterClient client = mock(FrankfurterClient.class);
        SpreadFactorCalculator calculator = mock(SpreadFactorCalculator.class);

        LatestRatesResponse mockResponse = new LatestRatesResponse();
        mockResponse.setAmount(1.0);
        mockResponse.setBase("IDR");
        mockResponse.setRates(Map.of("USD", 0.00006));

        when(client.getLatestIdrRates()).thenReturn(mockResponse);
        when(calculator.calculateSpreadFactor()).thenReturn(0.15); 

        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(client, calculator);

        List<LatestRatesResponse> result = fetcher.fetchData();

        assertEquals(1, result.size());
        LatestRatesResponse res = result.get(0);

        assertEquals(1.0, res.getAmount());
        assertEquals("IDR", res.getBase());
        assertEquals(0.00006, res.getRates().get("USD"));

        double expectedBuySpread = (1 / 0.00006) * 1.15;
        assertEquals(expectedBuySpread, res.getUsdBuySpreadIdr());
    }
}
