package com.bank.allo.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpreadCalculatorTest {

    @Test
    void testCalculateSpreadFactorWithNormalUsername() {
        double result = SpreadCalculator.calculateSpreadFactor("putrasaputra");

        int sum = "putrasaputra".chars().sum();
        double expected = (sum % 1000) / 100000.0;

        assertEquals(expected, result);
    }

    @Test
    void testCalculateSpreadFactorIsCaseInsensitive() {
        double lower = SpreadCalculator.calculateSpreadFactor("putra123");
        double upper = SpreadCalculator.calculateSpreadFactor("PUTRA123");

        assertEquals(lower, upper);
    }

    @Test
    void testCalculateSpreadFactorWithNullUsername() {
        double result = SpreadCalculator.calculateSpreadFactor(null);

        int sum = "anonymous".chars().sum();
        double expected = (sum % 1000) / 100000.0;

        assertEquals(expected, result);
    }

    @Test
    void testSpreadFactorRange() {
        double result = SpreadCalculator.calculateSpreadFactor("anything");

        assertTrue(result >= 0.0 && result <= 0.00999);
    }
}
