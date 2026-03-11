package com.api.allorestapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class SpreadCalculator {

    private final double spreadFactor;

    public SpreadCalculator(@Value("${frankfurter.github-username:mrafi68}") String githubUsername) {
        String lower = githubUsername.toLowerCase();
        int unicodeSum = lower.chars().sum();
        this.spreadFactor = (unicodeSum % 1000) / 100000.0;
    }

    public BigDecimal calculate(BigDecimal rateUsd) {
        BigDecimal inverse = BigDecimal.ONE.divide(rateUsd, new MathContext(15, RoundingMode.HALF_UP));
        BigDecimal factor  = BigDecimal.ONE.add(BigDecimal.valueOf(spreadFactor));
        return inverse.multiply(factor).setScale(8, RoundingMode.HALF_UP);
    }

    public double getSpreadFactor() {
        return spreadFactor;
    }
}
