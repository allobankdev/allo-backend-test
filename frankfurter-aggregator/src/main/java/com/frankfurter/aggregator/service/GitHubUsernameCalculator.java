package com.frankfurter.aggregator.service;

import com.frankfurter.aggregator.config.AppProperties;
import org.springframework.stereotype.Component;

@Component
public class GitHubUsernameCalculator {
    private final AppProperties appProperties;
    
    public GitHubUsernameCalculator(AppProperties appProperties) {
        this.appProperties = appProperties;
    }
    
    public String getGithubUsername() {
        return appProperties.getGithub().getUsername();
    }
    
    public double calculateSpreadFactor() {
        String username = getGithubUsername();
        if (username == null || username.trim().isEmpty()) {
            return 0.05; // Default if no username
        }
        
        String lowerUsername = username.toLowerCase();
        
        // Calculate sum of Unicode (ASCII) values
        int unicodeSum = 0;
        for (int i = 0; i < lowerUsername.length(); i++) {
            unicodeSum += (int) lowerUsername.charAt(i);
        }
        
        double spreadFactor = (unicodeSum % 1000) / 100000.0;
        
        // System.out.println("GitHub Username: " + username);
        // System.out.println("Lowercase: " + lowerUsername);
        // System.out.println("Sum of Unicode values: " + unicodeSum);
        // System.out.println("Sum % 1000: " + (unicodeSum % 1000));
        // System.out.println("Calculated Spread Factor: " + spreadFactor);
        
        return spreadFactor;
    }
}