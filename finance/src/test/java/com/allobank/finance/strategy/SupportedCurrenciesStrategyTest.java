package com.allobank.finance.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.allobank.finance.client.FrankfurterClient;

class SupportedCurrenciesStrategyTest {

    @Mock
    private FrankfurterClient client;

    private SupportedCurrenciesStrategy strategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        strategy = new SupportedCurrenciesStrategy(client);
    }

    @Test
    void fetch_shouldReturnSupportedCurrencies() {
        // given
        Map<String, Object> mockResponse = Map.of(
                "USD", "United States Dollar",
                "IDR", "Indonesian Rupiah"
        );

        when(client.getSupportedCurrencies()).thenReturn(mockResponse);

        // when
        Object result = strategy.fetch();

        // then
        assertEquals(mockResponse, result);
        verify(client).getSupportedCurrencies();
    }

    @Test
    void getResourceType_shouldReturnSupportedCurrencies() {
        assertEquals("supported_currencies", strategy.getResourceType());
    }
}
