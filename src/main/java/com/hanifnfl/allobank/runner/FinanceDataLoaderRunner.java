package com.hanifnfl.allobank.runner;

import com.hanifnfl.allobank.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinanceDataLoaderRunner implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> strategies;
    private final WebClient frankfurterClient;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting initial data load for all resource types...");

        strategies.forEach((key, strategy) -> {
            try {
                strategy.loadData(frankfurterClient);
                log.info("Successfully loaded resourceType={}", key);
            } catch (Exception ex) {
                log.error("Failed to load resourceType={}", key, ex);
            }
        });

        log.info("Initial data load completed.");
    }
}
