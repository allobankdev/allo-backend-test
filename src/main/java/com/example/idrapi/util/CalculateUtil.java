package com.example.idrapi.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalculateUtil {

    public static double calculateSpreadFactor(String githubUsername) {
        if (githubUsername == null || githubUsername.isBlank()) {
            throw new IllegalArgumentException("GitHub username must not be blank");
        }
        int sum = githubUsername.toLowerCase().chars().sum();
        log.info("spreadFactor {}", sum);
        return (sum % 1000) / 100_000.0;
    }
}
