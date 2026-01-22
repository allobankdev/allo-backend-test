package com.allobank.idr_rate_aggregator.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SpreadCalculator.
 */
class SpreadCalculatorTest {

    private SpreadCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new SpreadCalculator();
    }

    @Test
    void testCalculateSpreadFactor_ValidUsername() {
        // Test with a known username
        String username = "boysimbolon";
        double spreadFactor = calculator.calculateSpreadFactor(username);
        
        // Spread factor should be between 0.0032 and 0.0039
        assertTrue(spreadFactor >= 0.0032 && spreadFactor <= 0.0039,
                "Spread factor should be in range [0.0032, 0.0039]");
    }

    @Test
    void testCalculateSpreadFactor_Consistency() {
        // Same username should always produce same spread factor
        String username = "testuser";
        double factor1 = calculator.calculateSpreadFactor(username);
        double factor2 = calculator.calculateSpreadFactor(username);
        
        assertEquals(factor1, factor2, 0.00001,
                "Same username should produce consistent spread factor");
    }

    @Test
    void testCalculateSpreadFactor_EmptyUsername() {
        // Empty username should return base spread
        double spreadFactor = calculator.calculateSpreadFactor("");
        assertEquals(0.0032, spreadFactor, 0.00001);
    }

    @Test
    void testCalculateSpreadFactor_NullUsername() {
        // Null username should return base spread
        double spreadFactor = calculator.calculateSpreadFactor(null);
        assertEquals(0.0032, spreadFactor, 0.00001);
    }

    @Test
    void testCalculateUsdBuySpread_ValidInputs() {
        // Test buy spread calculation
        double usdRate = 0.000063; // Example: 1 IDR = 0.000063 USD
        double spreadFactor = 0.0035; // 0.35%
        
        double buySpread = calculator.calculateUsdBuySpread(usdRate, spreadFactor);
        
        // Buy spread should be positive
        assertTrue(buySpread > 0, "Buy spread should be positive");
        
        // Verify calculation: 1 / (0.000063 * (1 - 0.0035))
        double expected = 1.0 / (usdRate * (1.0 - spreadFactor));
        assertEquals(expected, buySpread, 0.01);
    }

    @Test
    void testCalculateUsdBuySpread_ZeroRate() {
        // Zero rate should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateUsdBuySpread(0.0, 0.0035);
        });
    }

    @Test
    void testCalculateUsdBuySpread_NegativeRate() {
        // Negative rate should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateUsdBuySpread(-0.000063, 0.0035);
        });
    }

    @Test
    void testCalculateUsdBuySpread_InvalidSpreadFactor() {
        // Spread factor > 1 should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateUsdBuySpread(0.000063, 1.5);
        });
    }

    @Test
    void testCalculateUsdBuySpread_NegativeSpreadFactor() {
        // Negative spread factor should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateUsdBuySpread(0.000063, -0.1);
        });
    }

    @Test
    void testSpreadFactorRange() {
        // Test that different usernames produce factors in expected range
        String[] usernames = {"aha", "boasa", "cantika", "darwin", "eesha", "fajar", "gilang", "hanna"};
        
        for (String username : usernames) {
            double factor = calculator.calculateSpreadFactor(username);
            // Use epsilon for floating-point comparison to handle precision errors
            assertTrue(factor >= 0.0032 - 1e-10 && factor <= 0.0039 + 1e-10,
                    "Spread factor for " + username + " should be in valid range (got " + factor + ")");
        }
    }
}

