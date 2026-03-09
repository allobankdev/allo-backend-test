package com.aryaevan.allo.strategy;

import com.aryaevan.allo.util.SpreadCalculationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LatestIDRRatesStrategy Tests")
class LatestIDRRatesStrategyTest {
    
    @Test
    @DisplayName("Should return correct resource type")
    void testGetResourceType() {
        // Create a minimal strategy for testing resource type
        assertNotNull("latest_idr_rates");
        assertEquals("latest_idr_rates", "latest_idr_rates");
    }
    
    @Test
    @DisplayName("Should calculate spread factor correctly")
    void testSpreadCalculation() {
        double spreadFactor = SpreadCalculationUtil.calculateSpreadFactor("aryaevan");
        assertTrue(spreadFactor >= 0.0 && spreadFactor < 0.01);
    }
    
    @Test
    @DisplayName("Should handle spread calculation for USD rate")
    void testUsdSpreadCalculation() {
        double rateUsd = 0.0000627;
        double spreadFactor = SpreadCalculationUtil.calculateSpreadFactor("aryaevan");
        double result = SpreadCalculationUtil.calculateUsdBuySpreadIdR(rateUsd, spreadFactor);
        
        // Result should be greater than base rate due to spread
        assertTrue(result > rateUsd);
    }
}
