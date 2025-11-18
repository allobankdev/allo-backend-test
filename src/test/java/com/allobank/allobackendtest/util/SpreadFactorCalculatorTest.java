package com.allobank.allobackendtest.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpreadFactorCalculatorTest {

    @Test
    void calculateSpreadFactor_lowercasesUsernameAndProducesValueInExpectedRange() {
        double factor = SpreadFactorCalculator.calculateSpreadFactor("TestUser123");

        assertTrue(factor >= 0.0 && factor < 0.01,
                "Spread factor harus berada di antara 0.00000 dan 0.00999");
    }

    @Test
    void calculateSpreadFactor_isDeterministicForSameUsername() {
        double factor1 = SpreadFactorCalculator.calculateSpreadFactor("fabrianivan");
        double factor2 = SpreadFactorCalculator.calculateSpreadFactor("FabrianIvan"); // beda kapital

        assertEquals(factor1, factor2, 0.00000001,
                "Spread factor harus konsisten untuk username yang sama (case-insensitive)");
    }

    @Test
    void calculateSpreadFactor_throwsExceptionWhenUsernameNull() {
        assertThrows(IllegalArgumentException.class,
                () -> SpreadFactorCalculator.calculateSpreadFactor(null));
    }
}
