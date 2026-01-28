package com.example.demo.util;

public final class SpreadCalculator {

    private SpreadCalculator() {
    }

    public static double calculate(String githubUsername) {

        if (githubUsername == null || githubUsername.isBlank()) {
            throw new IllegalArgumentException("GitHub username cannot be empty");
        }

        int sum = githubUsername
                .toLowerCase()
                .chars()
                .sum();

        return (sum % 1000) / 100000.0;
    }
}
