package com.allobank.finnance.allobankfinance.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class SpreadFactorCalculatorUtil {

    public static BigDecimal calculateSpreadFactor(String githubUsername) {
        if (githubUsername == null || githubUsername.isBlank()) {
            throw new IllegalArgumentException("githubUsername must not be null or blank");
        }

        int sum = githubUsername
                .toLowerCase()
                .chars()
                .sum();

        return BigDecimal.valueOf(sum % 1000)
                .divide(BigDecimal.valueOf(100000), 5, RoundingMode.HALF_UP);
    }
}
