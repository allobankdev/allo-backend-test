package com.allo.finance.runner;

import com.allo.finance.service.FinanceDataService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class FinanceStartupRunner implements ApplicationRunner {

    private final FinanceDataService service;

    public FinanceStartupRunner(FinanceDataService service) {
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) {
        service.loadAll();
    }
}
