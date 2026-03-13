package com.allobank.backend.test.strategy;

import com.allobank.backend.test.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class StrategyTests {

    @Mock
    private DataStore dataStore;

    @InjectMocks
    private LatestRatesStrategy latestRatesStrategy;

    @InjectMocks
    private HistoricalRatesStrategy historicalRatesStrategy;

    @InjectMocks
    private CurrenciesStrategy currenciesStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLatestRatesStrategy_Execute() {
        LatestRatesResponse mockResponse = new LatestRatesResponse();
        mockResponse.setBase("IDR");
        mockResponse.setUsdBuySpreadIdr(15500.0);
        when(dataStore.getLatestRates()).thenReturn(mockResponse);

        ApiResult result = latestRatesStrategy.execute();

        assertEquals("latest_idr_rates", result.getResource());
        assertEquals(mockResponse, result.getData());
    }

    @Test
    void testHistoricalRatesStrategy_Execute() {
        HistoricalRatesResponse mockResponse = new HistoricalRatesResponse();
        mockResponse.setBase("IDR");
        when(dataStore.getHistoricalRates()).thenReturn(mockResponse);

        ApiResult result = historicalRatesStrategy.execute();

        assertEquals("historical_idr_usd", result.getResource());
        assertEquals(mockResponse, result.getData());
    }

    @Test
    void testCurrenciesStrategy_Execute() {
        CurrenciesResponse mockResponse = new CurrenciesResponse();
        mockResponse.setCurrencies(Map.of("USD", "United States Dollar"));
        when(dataStore.getCurrencies()).thenReturn(mockResponse);

        ApiResult result = currenciesStrategy.execute();

        assertEquals("supported_currencies", result.getResource());
        assertEquals(mockResponse, result.getData());
    }
}