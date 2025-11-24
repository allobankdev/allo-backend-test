package com.test.allo_bank_test_exhange_rate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.test.allo_bank_test_exhange_rate.util.SpreadUtil;

public class SpreadUtilTest {
    
    @Test
    void testCalculateSpreadUsingGithubUsername() {
        double s = SpreadUtil.calculateSpread("Schwanzeirs");
        assertEquals(0.00201, s);
    }

    @Test
    void testCalculateSpreadUsingEmptyUsername() {
        double s = SpreadUtil.calculateSpread("");
        assertEquals(0.0, s);
    }
}
