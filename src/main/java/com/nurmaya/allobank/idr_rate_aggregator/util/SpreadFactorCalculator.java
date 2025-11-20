package com.nurmaya.allobank.idr_rate_aggregator.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpreadFactorCalculator {
    @Value("${github.username}")
    private String githubUsername;

    public double calculateSpreadFactor() {

        if (githubUsername == null || githubUsername.isEmpty()) {
            throw new IllegalStateException("GitHub username is not configured!");
        }

        String lower = githubUsername.toLowerCase();
        int sum = 0;

        for (char c : lower.toCharArray()) {
            sum += (int) c;
        }

        return (sum % 1000) / 100000.0;
    }
}
