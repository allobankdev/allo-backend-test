package com.finance.service.util;

public class SpreadCalculator {
    public static double computeSpread(String githubUsernameLowercase) {
        if (githubUsernameLowercase == null || githubUsernameLowercase.isBlank()) return 0.0;
        int sum = githubUsernameLowercase.chars().sum(); // Unicode code points (works for ascii)
        return (sum % 1000) / 100000.0;
    }
}
