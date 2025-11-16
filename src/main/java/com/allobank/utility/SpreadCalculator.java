package com.allobank.utility;

import com.allobank.config.AppProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Slf4j
public class SpreadCalculator {

    /**
     * -- GETTER --
     *  Returns the GitHub username used for calculation
     */
    @Getter
    private final String githubUsername;
    private final BigDecimal cachedSpreadFactor;

    public SpreadCalculator(AppProperties appProperties) {
        this.githubUsername = appProperties.getGithubUsername();
        this.cachedSpreadFactor = computeSpreadFactor();
        log.info("Initialized SpreadCalculator for username: {} with factor: {}",
                githubUsername, cachedSpreadFactor);
    }

    /**
     * Returns the calculated spread factor (cached after first computation)
     */
    public BigDecimal calculateSpreadFactor() {
        return cachedSpreadFactor;
    }

    /**
     * Computes the spread factor based on the GitHub username
     */
    private BigDecimal computeSpreadFactor() {
        String lowercase = githubUsername.toLowerCase();
        int sum = 0;

        for (char c : lowercase.toCharArray()) {
            sum += c;
        }

        log.info("Unicode sum for '{}': {}", lowercase, sum);

        // Spread Factor = (Sum % 1000) / 100000.0
        int modValue = sum % 1000;
        BigDecimal factor = BigDecimal.valueOf(modValue)
                .divide(BigDecimal.valueOf(100000.0), 10, RoundingMode.HALF_UP);

        log.info("Calculated spread factor: {} (from sum: {}, mod: {})",
                factor, sum, modValue);

        return factor;
    }

}
