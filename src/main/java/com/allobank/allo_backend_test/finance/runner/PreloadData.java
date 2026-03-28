package com.allobank.allo_backend_test.finance.runner;

import com.allobank.allo_backend_test.finance.config.AppConfig;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;
import com.allobank.allo_backend_test.finance.service.FinanceService;
import com.allobank.allo_backend_test.finance.service.strategy.FinanceResourceRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PreloadData implements ApplicationRunner {

    private final FinanceService financeService;
    private final FinanceResourceRegistry registry;
    private final AppConfig appConfig;
    private final FinanceRepository repository;

    @Override
    public void run(ApplicationArguments args) {
        log.info("finance data preload...");

        RetryPolicy retryPolicy = new RetryPolicy(
                appConfig.getPreload().getAttempt(),
                appConfig.getPreload().getBackoff()
        );

        for (String resourceType : registry.getHandlerMap().keySet()) {
            boolean success = retryPolicy.execute(resourceType, () -> financeService.fetchByResourceType(resourceType));

            if (!success) {
                log.error("All attempts exhausted '{}'. Failing silently.", resourceType);
            } else {
                log.info("Preloaded success: {}", resourceType);
            }
        }

        repository.lock();

        log.info("Finance data preload complete.");
    }
}