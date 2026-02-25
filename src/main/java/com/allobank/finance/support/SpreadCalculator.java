package com.allobank.finance.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class SpreadCalculator {

    public BigDecimal calculate(String githubUsername) {
        String lowercase = githubUsername.toLowerCase();
        int sum = 0;

        for (char c : lowercase.toCharArray()) {
            sum += c;
        }

        log.debug("Sum of Unicode values for '{}': {}", githubUsername, sum);

        double spreadFactor = (sum % 1000) / 100000.0;
        return BigDecimal.valueOf(spreadFactor);
    }

}
