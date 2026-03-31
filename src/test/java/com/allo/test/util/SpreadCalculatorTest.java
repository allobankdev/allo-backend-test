package com.allo.test.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpreadCalculatorTest {

    @Test
    void shouldCalculateSpreadCorrectly() {
        SpreadCalculator calculator = new SpreadCalculator();

        double spread = calculator.getSpread();

        assertTrue(spread >= 0);
        assertTrue(spread < 0.01);
    }
}