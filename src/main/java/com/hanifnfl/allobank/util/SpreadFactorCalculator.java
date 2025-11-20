package com.hanifnfl.allobank.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

public final class SpreadFactorCalculator {

    private SpreadFactorCalculator() {}

    public static BigDecimal calculateSpreadFactor(String githubUsername) {
        String normalized = githubUsername.toLowerCase(Locale.ROOT);
        int sum = normalized.chars().sum();
        int mod = sum % 1000;
        return BigDecimal.valueOf(mod)
                .divide(BigDecimal.valueOf(100000), 5, RoundingMode.HALF_UP);
    }
}
