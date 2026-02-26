package org.allobank.com.finanacedataservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class SpreadFactorCalculator {

    private final String githubUsername;

    public SpreadFactorCalculator(@Value(value = "${app.github-username}") String githubUsername) {
        this.githubUsername = githubUsername;
    }

    public BigDecimal calculateSpreadFactor() {
        String value = githubUsername == null ? "" : githubUsername.toLowerCase();
        int sum = 0;
        for (int i = 0; i < value.length(); i++) {
            sum += value.charAt(i);
        }
        int mod = sum % 1000;
        BigDecimal numerator = BigDecimal.valueOf(mod);
        BigDecimal denominator = BigDecimal.valueOf(100_000);
        return numerator.divide(denominator,5, RoundingMode.HALF_UP);
    }
}
