package com.allobank.finance.strategy;

import com.allobank.finance.client.FrankfurterClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
public class SupportedCurrenciesFetcherTest {

    @Mock
    private FrankfurterClient frankfurterClient;

    @InjectMocks
    private SupportedCurrenciesFetcher supportedCurrenciesFetcher;

    @Test
    void returnCurrenciesFromClient() {

        // GIVEN
        Map<String, String> mockCurrencies = Map.of("USD", "United States Dollar");

        when(frankfurterClient.getCurrencies()).thenReturn(mockCurrencies);

        // WHEN
        Object result = supportedCurrenciesFetcher.fetch();

        // THEN
        assertSame(mockCurrencies, result);
        verify(frankfurterClient).getCurrencies();
    }
}
