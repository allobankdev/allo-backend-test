package com.allo.bank.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.allo.bank.config.AppProperties;

@Component
public class SpreadFactorCalculator {

    private final AppProperties appProperties;

    public SpreadFactorCalculator(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public BigDecimal calculateSpreadFactor() {
        String username = appProperties.getGithubUsername();
        if (username == null || username.isBlank()) {
            return BigDecimal.ZERO.setScale(5, RoundingMode.HALF_UP);
        }

        int unicodeSum = username.toLowerCase().chars().sum();
        return BigDecimal.valueOf(unicodeSum % 1000L)
            .divide(BigDecimal.valueOf(100000L), 5, RoundingMode.HALF_UP);
    }
}
