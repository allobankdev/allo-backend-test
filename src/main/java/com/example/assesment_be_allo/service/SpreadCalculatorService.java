package com.example.assesment_be_allo.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SpreadCalculatorService {

    @Value("${assesment-be-allo.github.username:irwinekabakti}")
    private String githubUsername;

    public double calculateSpreadFactor() {
        return calculateSpreadFactor(githubUsername);
    }

    public double calculateSpreadFactor(String username) {
        String lowercase = username.toLowerCase();
        int sum = 0;
        for (char c : lowercase.toCharArray()) {
            sum += (int) c;
        }
        return (sum % 1000) / 100000.0;
    }

    public Double calculateUsdBuySpreadIdr(Double usdRate, double spreadFactor) {
        if (usdRate == null || usdRate == 0) {
            return null;
        }
        return (1.0 / usdRate) * (1.0 + spreadFactor);
    }

    public String getGithubUsername() {
        return githubUsername;
    }
}