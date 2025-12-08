package com.tes.allo.util;

public final class SpreadCalculator {
    private SpreadCalculator() {}

    public static double computeSpreadFactor(String githubUsername) {
        if (githubUsername == null) {
            throw new IllegalArgumentException("githubUsername must be provided in properties");
        }
        String lower = githubUsername.toLowerCase();
        int sum = 0;
        for (int i = 0; i < lower.length(); i++) sum += lower.charAt(i);
        int mod = sum % 1000;
        return mod / 100000.0;
    }
}
