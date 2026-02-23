package com.allobank.finance.idrrateaggregator.util;

public class SpreadCalculator {

    private SpreadCalculator() {}

    public static double calculateSpreadFactor(String githubUsername) {
        int sum = githubUsername
                .toLowerCase()
                .chars()
                .sum();

        return (sum % 1000) / 100000.0;
    }
}
