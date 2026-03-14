package com.allobank.allobackendtest.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpreadFactorCalculator {
    @Value("${app.github.username}")
    private String githubUsername;

    public double calculate() {
        int sum = 0;
        for(char c: githubUsername.toLowerCase().toCharArray()){
            sum += c;
        }
        log.info("Github Username {}, Sum ASCII: {}", githubUsername, sum);
        return (sum % 1000) / 100000.0;
    }
}
