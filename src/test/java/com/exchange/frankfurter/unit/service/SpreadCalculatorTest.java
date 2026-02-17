package com.exchange.frankfurter.unit.service;

import com.exchange.frankfurter.service.SpreadCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SpreadCalculatorTest {

    private final SpreadCalculator spreadCalculator = new SpreadCalculator();

    @Test
    @DisplayName("Should calculate spread factor correctly based on ASCII sum of 'xxx'")
    void testCalculateSpreadFactor() {
        // GIVEN: Username di dalam class adalah "xxx" (hardcoded di code Anda)
        // Manual Calculation:
        // sum of 'luthfiyyah-a' = 1237 (ASCII)
        // (1237 % 1000) = 237
        // 237 / 100000.0 = 0.00237

        double expectedFactor = 0.00237;

        // WHEN
        double actualFactor = spreadCalculator.calculateSpreadFactor();

        // THEN
        assertThat(actualFactor)
                .withFailMessage("Spread factor calculation logic mismatch!")
                .isEqualTo(expectedFactor);

        // Atau menggunakan JUnit 5 standar dengan delta (tolerance)
        // karena tipe data double seringkali punya presisi kecil di belakang
        assertEquals(expectedFactor, actualFactor, 0.00001);
    }

    @Test
    @DisplayName("Should always return a positive factor or zero")
    void testFactorIsNonNegative() {
        double actualFactor = spreadCalculator.calculateSpreadFactor();
        assertThat(actualFactor).isGreaterThanOrEqualTo(0.0);
    }
}