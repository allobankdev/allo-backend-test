package com.allo.test.unit;

import com.allo.finance.util.SpreadUtil;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class SpreadUtilTest {

    @Test
    void shouldCalculateSpreadCorrectly() {
        SpreadUtil util = new SpreadUtil();

        ReflectionTestUtils.setField(util, "username", "haidir");

        double result = util.calculateSpread();

        int sum = "haidir".chars().sum();
        double expected = (sum % 1000) / 100000.0;

        assertEquals(expected, result);
    }

    @Test
    void shouldReturnSpreadWithinRange() {
        SpreadUtil util = new SpreadUtil();

        ReflectionTestUtils.setField(util, "username", "haidir");

        double result = util.calculateSpread();

        assertTrue(result >= 0);
        assertTrue(result < 0.01);
    }
}