package com.allo.idraggregator.application.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.allo.idraggregator.domain.model.Currency;
import com.allo.idraggregator.infrastructure.client.FrankfurterClient;

class SupportedCurrenciesFetcherTest {

    @Mock
    private FrankfurterClient client;

    private SupportedCurrenciesFetcher fetcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fetcher = new SupportedCurrenciesFetcher(client);
    }

    @Test
    void shouldFetchCurrenciesFromClient() {
        
        Currency mockCurrency = mock(Currency.class);
        when(client.getCurrencies()).thenReturn(mockCurrency);

        Currency result = fetcher.fetchData();

        assertEquals(mockCurrency, result);
    }
}