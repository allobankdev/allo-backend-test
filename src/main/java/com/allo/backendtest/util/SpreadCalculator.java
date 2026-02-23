package com.allo.backendtest.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SpreadCalculator {

    public static BigDecimal calculateSpreadFactor(String username) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("GitHub username must not be null");
        }
        int sum = username.toLowerCase()
                .chars()
                .sum();

        return BigDecimal.valueOf(sum % 1000)
                .divide(BigDecimal.valueOf(100000), 5, RoundingMode.HALF_UP);
    }
}
