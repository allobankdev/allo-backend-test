package com.allobank.finance.util;

import com.allobank.finance.config.properties.GithubProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(GithubProperties.class)
public class SpreadCalculator {

    private final GithubProperties githubProperties;

    public double calculateSpreadFactor() {
        var sum = githubProperties.username().chars().sum();
        return (sum % 1000) / 100000.0;
    }
}
