package com.allobank.finance.support;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class SpreadCalculatorTest {

    private final SpreadCalculator calculator = new SpreadCalculator();

    @Test
    void shouldCalculateSpreadFactorForManzoy() {
        // m(109) + a(97) + n(110) + z(122) + o(111) + y(121) = 670
        // (670 % 1000) / 100000.0 = 0.00670
        BigDecimal result = calculator.calculate("manzoy");

        assertThat(result).isEqualByComparingTo("0.00670");
    }

    @Test
    void shouldCalculateSpreadFactorForTest() {
        // t(116) + e(101) + s(115) + t(116) = 448
        // (448 % 1000) / 100000.0 = 0.00448
        BigDecimal result = calculator.calculate("test");

        assertThat(result).isEqualByComparingTo("0.00448");
    }

    @Test
    void shouldHandleUppercaseUsername() {
        BigDecimal lowercase = calculator.calculate("manzoy");
        BigDecimal uppercase = calculator.calculate("MANZOY");

        assertThat(uppercase).isEqualByComparingTo(lowercase);
    }

    @Test
    void shouldHandleMixedCaseUsername() {
        BigDecimal result = calculator.calculate("MaNzOy");

        assertThat(result).isEqualByComparingTo("0.00670");
    }
}
