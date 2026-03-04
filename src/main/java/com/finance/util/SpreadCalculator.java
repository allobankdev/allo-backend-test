package com.finance.util;

public class SpreadCalculator {

    public static double calculateSpreadFactor(String username){

        String lower = username.toLowerCase();

        int sum = 0;
        for (char c : lower.toCharArray()) {
            sum += (int) c;
        }

        return (sum % 1000) / 100000.0;
    }
}
