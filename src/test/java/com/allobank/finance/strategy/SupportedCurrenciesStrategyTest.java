package com.allobank.finance.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SupportedCurrenciesStrategy Unit Tests")
class SupportedCurrenciesStrategyTest {

    @Test
    @DisplayName("getResourceType() harus return 'supported_currencies'")
    void shouldReturnCorrectResourceType() {
        SupportedCurrenciesStrategy strategy = new SupportedCurrenciesStrategy(null);
        assertThat(strategy.getResourceType()).isEqualTo("supported_currencies");
    }
}