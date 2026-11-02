package com.allobank.splitbill.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

@Component
public class ServiceCharge {
    private final int percentage;

    public ServiceCharge(@Value("${app.github-username}") String githubUsername) {
        if (githubUsername == null || githubUsername.isBlank()) {
            throw new IllegalArgumentException("app.github-username must not be blank");
        }
        this.percentage = githubUsername.toLowerCase(Locale.ROOT).codePoints().sum() % 10;
    }

    public int percentage() {
        return percentage;
    }

    public BigDecimal amountFor(BigDecimal totalExpenses) {
        return totalExpenses.multiply(BigDecimal.valueOf(percentage))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
