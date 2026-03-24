package com.allo.test.service;
public class SpreadCalculator {

    public static double calculateSpreadFactor(String username) {
        int sum = 0;

        for (char c : username.toLowerCase().toCharArray()) {
            sum += (int) c;
        }

        return (sum % 1000) / 100000.0;
    }
}