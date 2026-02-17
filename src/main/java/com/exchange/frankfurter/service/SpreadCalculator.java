package com.exchange.frankfurter.service;

import org.springframework.stereotype.Component;

@Component
public class SpreadCalculator {

    private static final String GITHUB_USERNAME = "luthfiyyah-a";

    public double calculateSpreadFactor() {

        int sum = GITHUB_USERNAME.toLowerCase()
                .chars()
                .sum();

        return (sum % 1000) / 100000.0;
    }
}
