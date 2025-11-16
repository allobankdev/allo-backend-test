package com.allobank.allobackendtest.util;

public final class SpreadFactorCalculator {

    private SpreadFactorCalculator() {}

    public static double calculateSpreadFactor(String githubUsername) {
        if (githubUsername == null) {
            throw new IllegalArgumentException("GitHub username must not be null");
        }
        String lower = githubUsername.toLowerCase();

        int sum = 0;
        for (char c : lower.toCharArray()) {
            sum += c;
        }

        int mod = sum % 1000;
        return mod / 100000.0;  // 0.00000 - 0.00999
    }
}
