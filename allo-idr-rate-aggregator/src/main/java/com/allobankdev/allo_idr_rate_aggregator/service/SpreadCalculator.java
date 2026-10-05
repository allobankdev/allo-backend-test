package com.allobankdev.allo_idr_rate_aggregator.service;

import org.springframework.stereotype.Component;

@Component
public class SpreadCalculator {

    private static final String GITHUB_USERNAME = "JokoKusnandi";

    public double spreadFactor() {
        int sum = GITHUB_USERNAME.toLowerCase()
                .chars()
                .sum();
        return (sum % 1000) / 100000.0;
    }
}

