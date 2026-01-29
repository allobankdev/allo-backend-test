package com.allobank.financeaggregator.service;

import com.allobank.financeaggregator.config.FrankfurterProperties;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class SpreadFactorCalculator {

    private static final BigDecimal SPREAD_DIVISOR = new BigDecimal("100000");
    private final FrankfurterProperties properties;

    public SpreadFactorCalculator(FrankfurterProperties properties) {
        this.properties = properties;
    }

    public BigDecimal getSpreadFactor() {
        String username = properties.getGithubUsername();
        if (username == null || username.isBlank()) {
            throw new IllegalStateException("GitHub username must be configured for spread calculation");
        }

        String normalized = username.toLowerCase(Locale.ROOT);
        int sum = normalized.chars().sum();
        int mod = sum % 1000;
        return BigDecimal.valueOf(mod).divide(SPREAD_DIVISOR, 5, RoundingMode.HALF_UP);
    }
}
