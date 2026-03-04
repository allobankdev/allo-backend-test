package com.finance.strategy;

import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.finance.service.FinanceCacheService;

import static org.assertj.core.api.Assertions.assertThat;

class HistoricalRateStrategyTest {

    private FinanceCacheService cacheService;
    private HistoricalRateStrategy strategy;

    @BeforeEach
    void setup() {
        cacheService = mock(FinanceCacheService.class);
        strategy = new HistoricalRateStrategy(cacheService);
    }

    @Test
    void shouldReturnHistoricalDataFromCache() {

        Map<String, Object> mockData = Map.of(
                "some", "data"
        );

        when(cacheService.get("historical_idr_usd"))
                .thenReturn(mockData);

        Object result = strategy.execute();

        assertThat(result).isEqualTo(mockData);
    }
}
