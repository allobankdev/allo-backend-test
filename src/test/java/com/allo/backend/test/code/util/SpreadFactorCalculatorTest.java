package com.allo.backend.test.code.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpreadFactorCalculatorTest {

    @Test
    void testCalculateSpreadFactor_RadityaDito() {
        // radityadito: r(114) + a(97) + d(100) + i(105) + t(116) + y(121) + a(97) + d(100) + i(105) + t(116) + o(111) = 1182
        // (1182 % 1000) / 100000.0 = 182 / 100000.0 = 0.00182
        double spreadFactor = SpreadFactorCalculator.calculateSpreadFactor("RadityaDito");
        assertEquals(0.00182, spreadFactor, 0.00001);
    }

    @Test
    void testCalculateSpreadFactor_CaseInsensitive() {
        double upperCase = SpreadFactorCalculator.calculateSpreadFactor("RADITYADITO");
        double lowerCase = SpreadFactorCalculator.calculateSpreadFactor("radityadito");
        double mixedCase = SpreadFactorCalculator.calculateSpreadFactor("RadityaDito");

        assertEquals(upperCase, lowerCase);
        assertEquals(lowerCase, mixedCase);
    }

    @Test
    void testCalculateSpreadFactor_NullUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> SpreadFactorCalculator.calculateSpreadFactor(null));
    }

    @Test
    void testCalculateSpreadFactor_EmptyUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> SpreadFactorCalculator.calculateSpreadFactor(""));
    }

    @Test
    void testCalculateUSDBySpread() {
        SpreadFactorCalculator calculator = new SpreadFactorCalculator("RadityaDito");

        // USD rate = 0.00006, spread factor = 0.00182
        // USD_BuySpread_IDR = (1 / 0.00006) * (1 + 0.00182) = 16666.67 * 1.00182 = 16696.97
        double usdRate = 0.00006;
        double result = calculator.calculateUSDBySpread(usdRate);

        assertEquals(16696.97, result, 1.0, "Expected around 16696.97, got " + result);
    }

    @Test
    void testCalculateUSDBySpread_ZeroRate() {
        SpreadFactorCalculator calculator = new SpreadFactorCalculator("RadityaDito");
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateUSDBySpread(0.0));
    }

    @Test
    void testCalculateUSDBySpread_NegativeRate() {
        SpreadFactorCalculator calculator = new SpreadFactorCalculator("RadityaDito");
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateUSDBySpread(-0.00006));
    }

    @Test
    void testGetters() {
        SpreadFactorCalculator calculator = new SpreadFactorCalculator("RadityaDito");
        assertEquals("RadityaDito", calculator.getGithubUsername());
        assertEquals(0.00182, calculator.getSpreadFactor(), 0.00001);
    }
}
