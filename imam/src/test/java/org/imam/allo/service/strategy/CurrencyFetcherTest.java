package org.imam.allo.service.strategy;

import org.imam.allo.client.FrankfurterClient;
import org.imam.allo.dto.CurrenciesResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CurrencyFetcherTest {

    @Mock
    private FrankfurterClient client;

    @InjectMocks
    private CurrencyFetcher fetcher;

    @Test
    void shouldReturnCurrencies() {
        CurrenciesResponse mockResponse = new CurrenciesResponse();

        when(client.getCurrencies()).thenReturn(mockResponse);

        Object result = fetcher.fetchData();

        assertNotNull(result);
        assertEquals(mockResponse, result);

        verify(client, times(1)).getCurrencies();
    }
}