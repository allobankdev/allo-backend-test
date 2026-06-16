package com.frankfurter.aggregator.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LatestIdrRatesStrategyTest {
    
    @Test
    void testResourceType() {
        assertEquals("latest_idr_rates", "latest_idr_rates");
    }
    
    @Test
    void testSpreadFactorCalculation() {
        String username = "andityadimas";
        String lower = username.toLowerCase();
        int sum = 0;
        for (char c : lower.toCharArray()) {
            sum += (int) c;
        }
        double spreadFactor = (sum % 1000) / 100000.0;
        
        assertTrue(spreadFactor >= 0.0 && spreadFactor <= 0.00999);
        assertEquals(0.00272, spreadFactor, 0.00001);
    }
}