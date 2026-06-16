package com.example.idr.service.calculator;

import org.springframework.stereotype.Component;

@Component
public class SpreadCalculator {

    private static final String GITHUB_USERNAME = "dickywahyudi";

    public double calculateSpreadFactor() {
        int sum = GITHUB_USERNAME
                .toLowerCase()
                .chars()
                .sum();

        return (sum % 1000) / 100000.0;
    }
}
