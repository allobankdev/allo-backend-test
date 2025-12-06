package com.bank.allo.utils;

public final class SpreadCalculator {

    private SpreadCalculator() {}

    public static double calculateSpreadFactor(String githubUsername) {
        if (githubUsername == null) githubUsername = "anonymous";

        String s = githubUsername.toLowerCase();
        int sum = 0;

        for (char c : s.toCharArray()) {
            sum += (int) c;
        }

        return (sum % 1000) / 100000.0;
    }
}
