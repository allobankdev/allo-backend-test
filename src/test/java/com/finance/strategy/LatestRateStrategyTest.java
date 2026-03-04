package com.finance.strategy;

import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.finance.service.FinanceCacheService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class LatestRateStrategyTest {

    private FinanceCacheService cacheService;
    private LatestRateStrategy strategy;

    @BeforeEach
    void setup() {
        cacheService = mock(FinanceCacheService.class);
        strategy = new LatestRateStrategy(cacheService);
    }

    @Test
    void shouldCalculateUsdBuySpreadCorrectly() {

        Map<String, Object> mockData = Map.of(
            "base", "IDR",
            "rates", Map.of("USD", 0.000064)
        );

        when(cacheService.get("latest_idr_rates"))
            .thenReturn(mockData);

        Map<String, Object> result =
            (Map<String, Object>) strategy.execute();

        double spread = (double) result.get("USD_BuySpread_IDR");

        double expected =
                (1 / 0.000064) * (1 + 0.00838);

        assertThat(spread).isCloseTo(expected, within(0.0001));
    }
}
