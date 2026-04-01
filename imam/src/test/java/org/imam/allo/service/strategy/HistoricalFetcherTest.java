package org.imam.allo.service.strategy;

import org.imam.allo.client.FrankfurterClient;
import org.imam.allo.dto.HistoricalResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HistoricalFetcherTest {

    @Mock
    private FrankfurterClient client;

    @InjectMocks
    private HistoricalFetcher fetcher;

    @Test
    void shouldReturnHistoricalData() {
        HistoricalResponse mockResponse = new HistoricalResponse();

        when(client.getHistorical()).thenReturn(mockResponse);

        Object result = fetcher.fetchData();

        assertNotNull(result);
        assertEquals(mockResponse, result);

        verify(client, times(1)).getHistorical();
    }
}