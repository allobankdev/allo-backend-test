package com.allobank.backendtest.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class SpreadCalculator {
    private SpreadCalculator() {}

    public static BigDecimal computeSpreadFactor(String githubUsername) {
        if (githubUsername == null) throw new IllegalArgumentException("username missing");
        int sum = githubUsername.toLowerCase().chars().sum();
        int mod = sum % 1000;

        return BigDecimal.valueOf(mod)
                .divide(BigDecimal.valueOf(100000), 6, RoundingMode.HALF_UP);
    }
}
