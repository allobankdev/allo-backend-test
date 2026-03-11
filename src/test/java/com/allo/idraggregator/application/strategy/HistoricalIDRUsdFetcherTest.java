package com.allo.idraggregator.application.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.allo.idraggregator.domain.model.HistoricalRates;
import com.allo.idraggregator.infrastructure.client.FrankfurterClient;
import com.allo.idraggregator.infrastructure.config.properties.FrankfurterProperties;

class HistoricalIDRUsdFetcherTest {

    @Mock
    private FrankfurterClient client;

    @Mock
    private FrankfurterProperties properties;

    private HistoricalIDRUsdFetcher fetcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fetcher = new HistoricalIDRUsdFetcher(client, properties);
    }

    @Test
    void shouldFetchHistoricalRatesUsingProperties() {
        
        String dateRange = "30D";
        when(properties.historicalRange()).thenReturn(dateRange);

        HistoricalRates mockResponse = HistoricalRates.builder().build();

        when(client.getHistorical(dateRange, "IDR", "USD"))
                .thenReturn(mockResponse);

        HistoricalRates result = fetcher.fetchData();

        assertEquals(mockResponse, result);
    }
}