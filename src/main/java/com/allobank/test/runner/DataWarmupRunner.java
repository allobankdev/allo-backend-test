package com.allobank.test.runner;

import com.allobank.test.service.FinanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataWarmupRunner implements ApplicationRunner {

    private final FinanceService financeService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("STARTUP: Fetching data from Frankfurter API...");

        try {
            financeService.fetchAndCacheAllData().get();
            log.info("STARTUP: Data warmup completed successfully.");
        } catch (Exception e) {
            log.error("STARTUP: Failed to fetch initial data", e);
            // System.exit(1);
        }
    }
}