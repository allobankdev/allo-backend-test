package com.allobank.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SpreadCalculatorTest {

    @Test
    void testCalculateSpreadFactor_ValidUsername() {
        String username = "testuser";
        BigDecimal spreadFactor = SpreadCalculator.calculateSpreadFactor(username);
        
        assertNotNull(spreadFactor);
        assertTrue(spreadFactor.compareTo(BigDecimal.ZERO) >= 0);
        assertTrue(spreadFactor.compareTo(new BigDecimal("0.00999")) <= 0);
    }

    @Test
    void testCalculateSpreadFactor_CaseInsensitive() {
        String username1 = "TestUser";
        String username2 = "testuser";
        
        BigDecimal factor1 = SpreadCalculator.calculateSpreadFactor(username1);
        BigDecimal factor2 = SpreadCalculator.calculateSpreadFactor(username2);
        
        assertEquals(factor1, factor2);
    }

    @Test
    void testCalculateSpreadFactor_NullUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            SpreadCalculator.calculateSpreadFactor(null);
        });
    }

    @Test
    void testCalculateSpreadFactor_EmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            SpreadCalculator.calculateSpreadFactor("");
        });
    }

    @Test
    void testCalculateUsdBuySpreadIdr_ValidInputs() {
        BigDecimal usdRate = new BigDecimal("0.000064");
        BigDecimal spreadFactor = new BigDecimal("0.00765");
        
        BigDecimal result = SpreadCalculator.calculateUsdBuySpreadIdr(usdRate, spreadFactor);
        
        assertNotNull(result);
        assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
        
        // Verify formula: (1 / Rate_USD) * (1 + Spread Factor)
        BigDecimal expected = BigDecimal.ONE
                .divide(usdRate, 10, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.ONE.add(spreadFactor))
                .setScale(10, java.math.RoundingMode.HALF_UP);
        
        assertEquals(expected, result);
    }

    @Test
    void testCalculateUsdBuySpreadIdr_NullUsdRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            SpreadCalculator.calculateUsdBuySpreadIdr(null, new BigDecimal("0.00765"));
        });
    }

    @Test
    void testCalculateUsdBuySpreadIdr_ZeroUsdRate() {
        assertThrows(IllegalArgumentException.class, () -> {
            SpreadCalculator.calculateUsdBuySpreadIdr(BigDecimal.ZERO, new BigDecimal("0.00765"));
        });
    }

    @Test
    void testCalculateUsdBuySpreadIdr_NullSpreadFactor() {
        assertThrows(IllegalArgumentException.class, () -> {
            SpreadCalculator.calculateUsdBuySpreadIdr(new BigDecimal("0.000064"), null);
        });
    }
}

