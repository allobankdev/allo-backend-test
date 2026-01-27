package com.backend.allobank.util;

public class SpreadFactorCalculator {

    public static double calculate(String githubUsername) {
        int sum = githubUsername.toLowerCase()
                .chars()
                .sum();

        return (sum % 1000) / 100000.0;
    }
}
