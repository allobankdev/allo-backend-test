package com.aryaevan.allo.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SpreadCalculationUtil Tests")
class SpreadCalculationUtilTest {
    
    @Test
    @DisplayName("Should calculate spread factor correctly for aryaevan")
    void testCalculateSpreadFactorForAryaevan() {
        // aryaevan: a(97) + r(114) + y(121) + a(97) + e(101) + v(118) + a(97) + n(110) = 855
        // 855 % 1000 = 855
        // 855 / 100000.0 = 0.00855
        double result = SpreadCalculationUtil.calculateSpreadFactor("aryaevan");
        assertEquals(0.00855, result, 0.00001);
    }
    
    @Test
    @DisplayName("Should calculate spread factor correctly for different usernames")
    void testCalculateSpreadFactorForDifferentUsernames() {
        // Test with a different username
        double result = SpreadCalculationUtil.calculateSpreadFactor("johndoe");
        assertTrue(result >= 0.0 && result < 0.01, "Spread factor should be between 0 and 0.01");
    }
    
    @Test
    @DisplayName("Should be case-insensitive")
    void testSpreadFactorCaseInsensitivity() {
        double lower = SpreadCalculationUtil.calculateSpreadFactor("aryaevan");
        double upper = SpreadCalculationUtil.calculateSpreadFactor("ARYAEVAN");
        double mixed = SpreadCalculationUtil.calculateSpreadFactor("ArYaEvAn");
        
        assertEquals(lower, upper);
        assertEquals(lower, mixed);
    }
    
    @Test
    @DisplayName("Should calculate USD buy spread IDR correctly")
    void testCalculateUsdBuySpreadIdR() {
        double rateUsd = 0.0000627;
        double spreadFactor = 0.00855;
        
        double result = SpreadCalculationUtil.calculateUsdBuySpreadIdR(rateUsd, spreadFactor);
        
        // (1 / 0.0000627) * (1 + 0.00855)
        double expected = (1.0 / rateUsd) * (1.0 + spreadFactor);
        assertEquals(expected, result, 0.00000001);
    }
    
    @Test
    @DisplayName("Should handle edge case with zero spread factor")
    void testUsdBuySpreadWithZeroSpreadFactor() {
        double rateUsd = 0.0000627;
        double spreadFactor = 0.0;
        
        double result = SpreadCalculationUtil.calculateUsdBuySpreadIdR(rateUsd, spreadFactor);
        double expected = 1.0 / rateUsd;
        
        assertEquals(expected, result, 0.00000001);
    }
    
    @Test
    @DisplayName("Should handle edge case with maximum spread factor")
    void testUsdBuySpreadWithMaxSpreadFactor() {
        double rateUsd = 0.0000627;
        double spreadFactor = 0.00999;
        
        double result = SpreadCalculationUtil.calculateUsdBuySpreadIdR(rateUsd, spreadFactor);
        double expected = (1.0 / rateUsd) * (1.0 + spreadFactor);
        
        assertEquals(expected, result, 0.00000001);
    }
    
    @Test
    @DisplayName("Spread factor should always be between 0 and 0.01")
    void testSpreadFactorBounds() {
        for (String username : new String[]{"a", "test", "verylongusername", "x", "zzz"}) {
            double spreadFactor = SpreadCalculationUtil.calculateSpreadFactor(username);
            assertTrue(spreadFactor >= 0.0, "Spread factor should be >= 0");
            assertTrue(spreadFactor < 0.01, "Spread factor should be < 0.01");
        }
    }
}
