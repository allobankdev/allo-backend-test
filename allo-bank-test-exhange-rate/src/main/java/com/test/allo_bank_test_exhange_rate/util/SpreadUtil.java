package com.test.allo_bank_test_exhange_rate.util;

public class SpreadUtil {
    public static double calculateSpread(String githubUsername) {
        if (githubUsername == null) githubUsername = "";
        String lower = githubUsername.toLowerCase();
        int sum = 0;
        for (char c : lower.toCharArray()) sum += (int) c;
        int mod = sum % 1000;
        return mod / 100000.0;
    }
}
