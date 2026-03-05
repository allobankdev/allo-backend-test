package com.allobank.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SpreadCalculator {

    @Value("${app.github-username}")
    private String githubUsername;

    public double calculate() {
        if (githubUsername == null || githubUsername.isEmpty()) {
            throw new IllegalStateException("GitHub username not configured!");
        }
        return calculateFromUsername(githubUsername);
    }
    public double calculateFromUsername(String username) {
        int sum = 0;
        for (char c : username.toLowerCase().toCharArray()) {
            sum += (int) c;
        }
        return (sum % 1000) / 100000.0;
    }
}