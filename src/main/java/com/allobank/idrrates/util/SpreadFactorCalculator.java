package com.allobank.idrrates.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class SpreadFactorCalculator {

    private SpreadFactorCalculator() {
    }

    public static BigDecimal calculate(String githubUsername) {
        int asciiSum = 0;
        for (char c : githubUsername.toCharArray()) {
            asciiSum += c;
        }
        int modValue = asciiSum % 1000;
        return BigDecimal.valueOf(modValue)
                .divide(BigDecimal.valueOf(100_000), 10, RoundingMode.HALF_UP);
    }
}
