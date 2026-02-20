package com.allobank.aggregator.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpreadCalculatorTest {

    @Test
    void computeSpreadFactor_shouldBeDeterministicAndCaseInsensitive() {
        double a = SpreadCalculator.computeSpreadFactor("HakimAmarullah");
        double b = SpreadCalculator.computeSpreadFactor("hakimamarullah");
        assertThat(a).isEqualTo(b);

        // Simple manual calculation for known value
        double c = SpreadCalculator.computeSpreadFactor("abc");
        // 'a'=97, 'b'=98, 'c'=99 => sum=294; 294%1000=294; 294/100000=0.00294
        assertThat(c).isEqualTo(0.00294d);
    }

    @Test
    void computeSpreadFactor_handlesNullAndEmpty() {
        assertThat(SpreadCalculator.computeSpreadFactor(null)).isEqualTo(0.0d);
        assertThat(SpreadCalculator.computeSpreadFactor("")).isEqualTo(0.0d);
    }
}
