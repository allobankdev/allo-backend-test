package com.allobank.finance.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpreadCalculatorTest {

    @Test
    void calculateUsdBuySpread_shouldReturnCorrectValue() {
        double rateUsd = 0.00006;
        String githubUsername = "hosea-adrianus";

        double expected = 16738.000000000004;
        double actual = SpreadCalculator.calculateUsdBuySpread(rateUsd, githubUsername);

        assertEquals(expected, actual);
    }
}