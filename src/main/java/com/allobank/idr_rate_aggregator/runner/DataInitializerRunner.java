package com.allobank.idr_rate_aggregator.runner;

import com.allobank.idr_rate_aggregator.service.FinanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializerRunner implements ApplicationRunner {

    private final FinanceService financeService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("=== DataInitializerRunner starting... ===");
        try {
            financeService.initializeData();
            log.info("=== DataInitializerRunner completed successfully. ===");
        } catch (Exception e) {
            log.error("=== DataInitializerRunner FAILED: {} ===", e.getMessage());
            throw e;
        }
    }
}
