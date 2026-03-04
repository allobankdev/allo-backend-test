package com.finance.strategy;

import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.finance.service.FinanceCacheService;

import static org.assertj.core.api.Assertions.assertThat;

class SupportedCurrenciesStrategyTest {

    private FinanceCacheService cacheService;
    private CurrencyListStrategy strategy;

    @BeforeEach
    void setup() {
        cacheService = mock(FinanceCacheService.class);
        strategy = new CurrencyListStrategy(cacheService);
    }

    @Test
    void shouldReturnCurrenciesFromCache() {

        Map<String, String> mockData = Map.of(
                "USD", "United States Dollar"
        );

        when(cacheService.get("supported_currencies"))
                .thenReturn(mockData);

        Object result = strategy.execute();

        assertThat(result).isEqualTo(mockData);
    }
}
