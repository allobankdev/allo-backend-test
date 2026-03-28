package com.allobank.allo_backend_test.finance.service;

import com.allobank.allo_backend_test.finance.config.AppConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpreadServiceImpl implements SpreadService {

    @Getter
    private final Double spreadFactor;

    public SpreadServiceImpl(AppConfig config) {
        log.info("username: '{}'", config.getGithubUsername());
        String username = config.getGithubUsername();
        int sum = username.toLowerCase().chars().sum();
        log.info("sum: '{}'", sum);
        this.spreadFactor = (sum % 1000) / 100000.0;
        log.info("spreadfactor: '{}'", String.format("%.7f", spreadFactor));
    }

    @Override
    public Double calculateSpread(Double rate) {
        return (1.0 / rate) * (1.0 + spreadFactor);
    }
}