package com.example.allotest.util;

public class SpreadCalculator {
    public static double calculate(String username) {
        int total = username.toLowerCase().chars().sum();
        return (total % 1000) / 100000.0;
    }
}
