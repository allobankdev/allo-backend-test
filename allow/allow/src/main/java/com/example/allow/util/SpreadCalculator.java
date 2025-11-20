package com.example.allow.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpreadCalculator {

    private final double spreadFactor;

    public SpreadCalculator(@Value("${github.username}") String githubUsername) {
        int sum = githubUsername.toLowerCase()
                .chars()
                .sum();
        this.spreadFactor = (sum % 1000) / 100000.0;
    }

    public double getSpreadFactor() {
        return spreadFactor;
    }
}
