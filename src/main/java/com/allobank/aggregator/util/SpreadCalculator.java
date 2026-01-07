package com.allobank.aggregator.util;

public final class SpreadCalculator {
    private SpreadCalculator() {}

    /**
     * Compute spread factor:
     * - lower-case username
     * - sum unicode values of characters
     * - (sum % 1000) / 100000.0
     */
    public static double computeSpreadFactor(String githubUsername) {
        if (githubUsername == null) githubUsername = "";
        String s = githubUsername.toLowerCase();
        int sum = 0;
        for (char c : s.toCharArray()) {
            sum += (int) c;
        }
        int mod = sum % 1000;
        return mod / 100000.0;
    }
}
