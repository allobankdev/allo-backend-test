package com.example.allobank.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component
public class SpreadFactorCalculator {

    public BigDecimal calculateFromUsername(String username) {

        int sum = 0;
        for (char c : username.toLowerCase().toCharArray()) {
            sum += c;
        }

        return BigDecimal
                .valueOf(sum % 1000)
                .divide(BigDecimal.valueOf(100000), 5, RoundingMode.HALF_UP);
    }
}
