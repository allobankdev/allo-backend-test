package com.allobank.backendtest.runner;

import com.allobank.backendtest.service.FinanceDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataLoaderRunner implements ApplicationRunner {
    private final FinanceDataService financeDataService;

    public DataLoaderRunner(FinanceDataService financeDataService) {
        this.financeDataService = financeDataService;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            financeDataService.loadAllData();
        } catch (Exception e) {
            throw new RuntimeException("Unable to load required finance data", e);
        }
    }
}
