package com.allobank.finance.util;

public final class SpreadCalculator {

    private SpreadCalculator() {}

    public static double calculateUsdBuySpread(double rateUsd, String githubUsername) {
        int sum = githubUsername.toLowerCase().chars().sum();
        double spread = (sum % 1000) / 100000.0;
        return (1 / rateUsd) * (1 + spread);
    }
}
