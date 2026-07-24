package com.allobankdev.splitbill.service;

import org.springframework.stereotype.Service;

@Service
public class PersonalizationService {

    /**
     * Calculates the service charge percentage based on a GitHub username.
     * 1. Take username in lowercase.
     * 2. Sum the Unicode (ASCII) values of all characters.
     * 3. service_charge_pct = (sum % 10)
     */
    public int calculateServiceChargePct(String githubUsername) {
        if (githubUsername == null || githubUsername.isEmpty()) {
            return 0;
        }
        
        String lowerCaseUsername = githubUsername.toLowerCase();
        int sum = 0;
        for (char c : lowerCaseUsername.toCharArray()) {
            sum += c;
        }
        
        return sum % 10;
    }
}
