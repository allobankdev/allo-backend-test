package com.allo.backendtest.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpreadCalculatorTest {

    @Test
    void shouldCalculateSpreadCorrectly() {
        BigDecimal spread = SpreadCalculator.calculateSpreadFactor("hollymolly2708");

        assertNotNull(spread);
        assertTrue(spread.compareTo(BigDecimal.ZERO) > 0);
    }
}