package com.example.allobank.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpreadCalculator {

    @Value("${github.username}")
    private String username;

    public double getSpreadFactor() {
        int sum = username.toLowerCase()
                .chars()
                .sum();
        return (sum % 1000) / 100000.0;
    }
}