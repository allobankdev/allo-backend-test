package com.allo.finance.loader;

import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.allo.finance.service.FinanceDataStore;
import com.allo.finance.strategy.IDRDataFetcher;

@Component
public class FinanceDataLoader implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> strategies;
    private final FinanceDataStore store;

    public FinanceDataLoader(
            Map<String, IDRDataFetcher> strategies,
            FinanceDataStore store) {
        this.strategies = strategies;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {
        strategies.forEach((beanName, strategy) -> {
            String resourceType = strategy.getResourceType();
            Object data = strategy.fetchData();
            store.put(resourceType, data);
        });
    }

}