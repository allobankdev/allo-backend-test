package com.allobank.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpreadFactorConfig {

    @Value("${app.finance.github-username}")
    private String githubUsername;

    @Bean(name = "spreadFactorValue")
    public Double spreadFactor() {
        int sum = githubUsername.toLowerCase()
                .chars()
                .sum();

        return (sum % 1000) / 100000.0;
    }
}