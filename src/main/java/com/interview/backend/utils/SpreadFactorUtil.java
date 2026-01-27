package com.interview.backend.utils;

public class SpreadFactorUtil {

    public static double calculateSpreadFactor(String githubUsername) {
        if (githubUsername == null || githubUsername.isEmpty()) {
            return 0.0;
        }

        String lowercase = githubUsername.toLowerCase();
        int sum = 0;
        for (char c : lowercase.toCharArray()) {
            sum += (int) c;
        }
        return (sum % 1000) / 100000.0;
    }

    public static double calculateUsdBuySpreadIdr(double usdRate, String githubUsername) {
        double spreadFactor = calculateSpreadFactor(githubUsername);
        return (1 / usdRate) * (1 + spreadFactor);
    }
}
