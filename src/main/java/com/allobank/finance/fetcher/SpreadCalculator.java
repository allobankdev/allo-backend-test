package com.allobank.finance.fetcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpreadCalculator {

    private final double spreadFactor;

    public SpreadCalculator(@Value("${app.github-username}") String githubUsername) {
        int sum = githubUsername.toLowerCase().chars().sum();
        this.spreadFactor = (sum % 1000) / 100000.0;
    }

    public double getSpreadFactor() {
        return spreadFactor;
    }


    public double calculateUsdBuySpreadIdr(double rateUsd) {
        return (1.0 / rateUsd) * (1.0 + spreadFactor);
    }
}
