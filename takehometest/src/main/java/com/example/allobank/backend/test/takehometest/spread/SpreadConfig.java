package com.example.allobank.backend.test.takehometest.spread;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpreadConfig {

    @Value("${app.github.username}")
    private String githubUsername;

    public double getSpreadFactor() {
        int sum = githubUsername
                .toLowerCase()
                .chars()
                .sum();
        return (sum % 1000) / 100000.0;
    }
}
