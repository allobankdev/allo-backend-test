package com.allobank.finance.strategy;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.dto.HistoricalResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HistoricalFetcherTest {

    @Mock
    private FrankfurterClient  frankfurterClient;

    @InjectMocks
    private HistoricalIdrUsdFetcher historicalIdrUsdFetcher;

    @Test
    void returnHistoricalDataFromClient() {

        // GIVEN
        HistoricalResponse historicalResponse = new HistoricalResponse();
        historicalResponse.setBaseCurrency("IDR");

        when(frankfurterClient.getHistoricalIdrUsd()).thenReturn(historicalResponse);

        // WHEN
        Object result = historicalIdrUsdFetcher.fetch();

        // THEN
        assertSame(historicalResponse, result);
        verify(frankfurterClient).getHistoricalIdrUsd();
    }
}
