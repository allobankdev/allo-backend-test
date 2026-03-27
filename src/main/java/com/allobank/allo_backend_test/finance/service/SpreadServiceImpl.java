package com.allobank.allo_backend_test.finance.service;

import com.allobank.allo_backend_test.finance.config.AppConfig;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class SpreadServiceImpl implements SpreadService {

    @Getter
    private final Double spreadFactor;

    public SpreadServiceImpl(AppConfig config) {
        String username = config.getGithubUsername();
        int sum = username.toLowerCase().chars().sum();
        this.spreadFactor = (sum % 1000) / 100000.0;
    }

    @Override
    public Double calculateSpread(Double rate) {
        return (1.0 / rate) * (1.0 + spreadFactor);
    }
}