package com.allobank.splitbill.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PersonalizationService {

    private final String githubUsername;

    public PersonalizationService(@Value("${app.github.username:resa-rm}") String githubUsername) {
        this.githubUsername = githubUsername;
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    /**
     * Calculates service_charge_pct based on GitHub username:
     * 1. Take username in lowercase
     * 2. Sum the Unicode (ASCII) values of all characters
     * 3. service_charge_pct = sum % 10
     */
    public int calculateServiceChargePct() {
        if (githubUsername == null || githubUsername.isBlank()) {
            return 0;
        }
        String usernameLower = githubUsername.trim().toLowerCase();
        int sum = 0;
        for (char c : usernameLower.toCharArray()) {
            sum += (int) c;
        }
        return sum % 10;
    }

    /**
     * Calculates service_charge_amount based on percentage applied to total expenses
     */
    public BigDecimal calculateServiceChargeAmount(BigDecimal totalExpenses) {
        if (totalExpenses == null || totalExpenses.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        int pct = calculateServiceChargePct();
        return totalExpenses.multiply(BigDecimal.valueOf(pct))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
