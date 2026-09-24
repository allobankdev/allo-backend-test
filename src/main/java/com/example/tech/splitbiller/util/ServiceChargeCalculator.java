package com.example.tech.splitbiller.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ServiceChargeCalculator {

    private final String githubUsername;

    public ServiceChargeCalculator(@Value("${app.github-username}") String githubUsername) {
        this.githubUsername = githubUsername;
    }

    public BigDecimal calculatePercentage() {
        int sum = githubUsername.chars().sum();
        return BigDecimal.valueOf(sum % 10);
    }

    public BigDecimal calculateAmount(BigDecimal totalExpenses) {
        BigDecimal percentage = calculatePercentage();
        return totalExpenses.multiply(percentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
