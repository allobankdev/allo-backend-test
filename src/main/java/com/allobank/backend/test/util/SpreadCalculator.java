package com.allobank.backend.test.util;

public class SpreadCalculator {

    public static double calculateSpreadFactor(String githubUsername) {
        int sum = 0;
        for (char c : githubUsername.toLowerCase().toCharArray()) {
            sum += (int) c;
        }
        return (sum % 1000) / 100000.0;
    }
}