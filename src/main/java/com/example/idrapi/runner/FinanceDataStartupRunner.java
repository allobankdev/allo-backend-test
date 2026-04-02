package com.example.idrapi.runner;

import com.example.idrapi.service.FinanceDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class FinanceDataStartupRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(FinanceDataStartupRunner.class);

    private final FinanceDataService financeDataService;

    public FinanceDataStartupRunner(FinanceDataService financeDataService) {
        this.financeDataService = financeDataService;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("=== FinanceDataStartupRunner: beginning pre-fetch of all IDR resources ===");
        try {
            financeDataService.loadAll();
            log.info("=== FinanceDataStartupRunner: pre-fetch complete. Application is ready. ===");
        } catch (Exception ex) {
            // Re-throw to fail fast — a broken data store means a broken API.
            log.error("Critical failure during startup data load. Application cannot serve requests.", ex);
            throw new RuntimeException("Startup data ingestion failed", ex);
        }
    }
}
