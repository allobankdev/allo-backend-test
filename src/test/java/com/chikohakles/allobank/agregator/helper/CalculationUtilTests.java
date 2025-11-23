package com.chikohakles.allobank.agregator.helper;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

class CalculationUtilTests {

    @Test
    void calculateSpreadFactor_ShouldCalculateBasedOnAsciiSumModulo1000() {
        BigDecimal factor = CalculationUtil.calculateSpreadFactor("chikohakles");

        assertThat(factor).isEqualByComparingTo(new BigDecimal("0.00158"));
    }

    @Test
    void calculateRate_ShouldReturnExpectedRate() {
        BigDecimal spreadFactor = new BigDecimal("0.00510");
        BigDecimal rate = new BigDecimal("16000.00");

        BigDecimal result = CalculationUtil.calculateRate(spreadFactor, rate);

        assertThat(result).isEqualByComparingTo(
                BigDecimal.ONE
                        .divide(rate, 5, RoundingMode.HALF_UP)
                        .multiply(spreadFactor.add(BigDecimal.ONE))
        );
    }
}

