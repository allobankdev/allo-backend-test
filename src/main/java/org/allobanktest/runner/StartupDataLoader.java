package org.allobanktest.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.allobanktest.store.FinancialDataStore;
import org.allobanktest.strategy.IDRDataFetcher;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;


import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartupDataLoader implements ApplicationRunner {
    private final FinancialDataStore store;
    private final WebClient frankfurterClient;
    private final Map<String, IDRDataFetcher> strategies;

    @Value("${app.github-username}")
    private String githubUsername;

    @Override
    public void run(@NonNull ApplicationArguments args) {
        log.info("Initializing data with GitHub username: {}", githubUsername);
        List<?> latest = strategies.get("latest_idr_rates").load(frankfurterClient, githubUsername);
        List<?> hist = strategies.get("historical_idr_usd").load(frankfurterClient, githubUsername);
        List<?> curr = strategies.get("supported_currencies").load(frankfurterClient, githubUsername);
        store.setLatestIdrRates(latest);
        store.setHistoricalIdrUsd(hist);
        store.setSupportedCurrencies(curr);
        log.info("Data initialized: latest={}, historical={}, currencies={}", latest.size(), hist.size(), curr.size());
    }
}
