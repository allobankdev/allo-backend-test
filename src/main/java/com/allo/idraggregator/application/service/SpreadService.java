package com.allo.idraggregator.application.service;

import org.springframework.stereotype.Service;

import com.allo.idraggregator.infrastructure.config.properties.GithubProperties;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SpreadService {

    private GithubProperties properties;

    public double getSpreadFactor() {

        int sum = properties.username().toLowerCase()
                .chars()
                .sum();

        return (sum % 1000) / 100000.0;
    }

    public double getUsdBuySpread(double rateUsd) {

        double spread = getSpreadFactor();

        return (1 / rateUsd) * (1 + spread);
    }
}