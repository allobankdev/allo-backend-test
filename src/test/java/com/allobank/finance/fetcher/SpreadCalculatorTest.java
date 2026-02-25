package com.allobank.finance.fetcher;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SpreadCalculatorTest {

    @Test
    void shouldCalculateCorrectSpreadForThaufaniqbal() {
        SpreadCalculator calculator = new SpreadCalculator("thaufaniqbal");

        assertThat(calculator.getSpreadFactor()).isEqualTo(0.00264);
    }

    @Test
    void shouldCalculateUsdBuySpreadIdrCorrectly() {
        SpreadCalculator calculator = new SpreadCalculator("thaufaniqbal");
        double rateUsd = 0.000064;

        double result = calculator.calculateUsdBuySpreadIdr(rateUsd);

        double expected = (1.0 / rateUsd) * (1.0 + 0.00264);
        assertThat(result).isCloseTo(expected, within(0.0001));
    }

    @Test
    void shouldHandleDifferentUsername() {

        SpreadCalculator calculator = new SpreadCalculator("johndoe47");
        assertThat(calculator.getSpreadFactor()).isEqualTo(0.00850);
    }

    @Test
    void shouldBeCaseInsensitive() {
        SpreadCalculator lower = new SpreadCalculator("thaufaniqbal");
        SpreadCalculator upper = new SpreadCalculator("THAUFANIQBAL");

        assertThat(lower.getSpreadFactor()).isEqualTo(upper.getSpreadFactor());
    }
}
