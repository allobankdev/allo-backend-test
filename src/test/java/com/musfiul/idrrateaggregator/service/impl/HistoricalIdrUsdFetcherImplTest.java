package com.musfiul.idrrateaggregator.service.impl;

import com.musfiul.idrrateaggregator.client.FrankfurterClient;
import com.musfiul.idrrateaggregator.dto.HistoricalRatesResponse;
import com.musfiul.idrrateaggregator.service.fetcher.impl.HistoricalIdrUsdFetcherImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalIdrUsdFetcherImplTest {

    @Mock
    private FrankfurterClient client;

    @InjectMocks
    private HistoricalIdrUsdFetcherImpl fetcher;

    @Test
    void shouldReturnCorrectResourceType() {
        assertEquals("HISTORICAL_IDR_USD", fetcher.getType().name());
    }

    @Test
    void shouldFetchHistoricalRatesFromClient() {
        HistoricalRatesResponse mockResponse = new HistoricalRatesResponse();

        when(client.getHistoricalRates()).thenReturn(mockResponse);

        HistoricalRatesResponse result = fetcher.fetchData();

        assertEquals(mockResponse, result);
        verify(client).getHistoricalRates();
        verifyNoMoreInteractions(client);
    }
}