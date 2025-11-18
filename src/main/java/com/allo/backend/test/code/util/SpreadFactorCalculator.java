package com.allo.backend.test.code.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpreadFactorCalculator {

    private final String githubUsername;
    private final double spreadFactor;

    public SpreadFactorCalculator(@Value("${github.username}") String githubUsername) {
        this.githubUsername = githubUsername;
        this.spreadFactor = calculateSpreadFactor(githubUsername);
        log.info("Initialized SpreadFactorCalculator for username: {} with spread factor: {}",
                 githubUsername, spreadFactor);
    }

    /**
     * Calculates the spread factor based on GitHub username.
     * Formula: (Sum of Unicode Values % 1000) / 100000.0
     */
    public static double calculateSpreadFactor(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("GitHub username cannot be null or empty");
        }

        String lowercase = username.toLowerCase();
        int sum = 0;

        for (char c : lowercase.toCharArray()) {
            sum += (int) c;
        }

        int modResult = sum % 1000;
        return modResult / 100000.0;
    }

    /**
     * Calculates USD buy spread in IDR.
     * Formula: USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
     */
    public double calculateUSDBySpread(double rateUSD) {
        if (rateUSD <= 0) {
            throw new IllegalArgumentException("Rate USD must be positive");
        }
        return (1.0 / rateUSD) * (1.0 + spreadFactor);
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    public double getSpreadFactor() {
        return spreadFactor;
    }
}
