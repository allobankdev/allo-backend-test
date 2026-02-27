package com.allobank.finance.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpreadCalculatorTest {

    @Test
    void calculateUsdBuySpread_shouldReturnCorrectValue() {
        double rateUsd = 15000.0;
        String githubUsername = "hosea-adrianus";

        double expected = 0.000066952;
        double actual = SpreadCalculator.calculateUsdBuySpread(rateUsd, githubUsername);

        assertEquals(expected, actual, 0.000000001);
    }
}