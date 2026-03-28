package com.allobank.allo_backend_test.finance.runner;

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

    @Override
    public void run(ApplicationArguments args) {
        log.info("finance data preload");
        for (String resourceType : registry.getHandlerMap().keySet()) {
            financeService.fetchByResourceType(resourceType);
            log.info("Preloaded: {}", resourceType);
        }
        log.info("Finance data preload complete.");
    }
}