package org.imam.allo.util;

public class SpreadCalculator {
    public static double calculate(String username) {
        int sum = username.chars().sum();
        return (sum % 1000) / 100000.0;
    }
}
