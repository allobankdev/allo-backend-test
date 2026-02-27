package com.amri.apiintegration.application.service;

import com.amri.apiintegration.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SpreadFactorService {

    private final ApplicationProperties applicationProperties;

    public BigDecimal getSpreadFactor() {
        String username = applicationProperties.githubUsername();
        if (username == null || username.isBlank()) {
            throw new IllegalStateException("app.github-username must be configured");
        }

        String normalized = username.toLowerCase(Locale.ROOT);
        int sum = 0;
        for (char c : normalized.toCharArray()) {
            sum += c;
        }

        return BigDecimal.valueOf(sum % 1000L)
                .divide(BigDecimal.valueOf(100000L), 5, RoundingMode.HALF_UP);
    }
}
