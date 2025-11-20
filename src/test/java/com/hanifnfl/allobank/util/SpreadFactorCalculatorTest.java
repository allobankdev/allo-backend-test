package com.hanifnfl.allobank.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class SpreadFactorCalculatorTest {

    @Test
    void shouldCalculateSpreadFactorForGithubUsername() {
        // given
        String username = "hanifnfl097";

        // when
        BigDecimal spreadFactor = SpreadFactorCalculator.calculateSpreadFactor(username);

        // then
        assertThat(spreadFactor)
                .isEqualByComparingTo(new BigDecimal("0.00998"));
    }
}
