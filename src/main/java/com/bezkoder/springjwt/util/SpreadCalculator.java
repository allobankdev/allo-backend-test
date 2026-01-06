package com.bezkoder.springjwt.util;

import com.bezkoder.springjwt.config.GitHubProperties;
import org.springframework.stereotype.Component;

@Component
public class SpreadCalculator {

    private final GitHubProperties gitHubProperties;

    public SpreadCalculator(GitHubProperties gitHubProperties) {
        this.gitHubProperties = gitHubProperties;
    }

    public double spreadFactor() {
        String username = gitHubProperties.getUsername().toLowerCase();
        System.out.println("username "+username);
        int sum = username.chars().sum();
        return (sum % 1000) / 100000.0;
    }

    public double usdBuySpreadIdr(double rateUsdFromBaseIdr) {
        if (rateUsdFromBaseIdr <= 0.0) {
            throw new IllegalArgumentException("Rate_USD must be > 0");
        }
        double spread = spreadFactor();
        return (1.0 / rateUsdFromBaseIdr) * (1.0 + spread);
    }
}
