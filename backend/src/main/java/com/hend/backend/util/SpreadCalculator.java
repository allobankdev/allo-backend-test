package com.hend.backend.util;

import org.springframework.stereotype.Component;

/**
 * @author : hend wunga
 */

@Component
public class SpreadCalculator {
    public double calculateSpread(String githubUsername) {
        if(githubUsername == null )return 0.0;
        String username=githubUsername.toLowerCase();
        int sum=0;
        for(char c:username.toCharArray()){
            sum+=(int)c;
        }
        // Rumus: (Sum % 1000) / 100000.0
        return (sum % 1000) / 100000.0;
    }
}
