package com.example.idr.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.idr.service.strategy.IDRDataFetcherRegistry;
import com.example.idr.service.store.FinanceDataStore;

@Component
public class FinanceDataInitializer implements ApplicationRunner {

    private final IDRDataFetcherRegistry registry;
    private final FinanceDataStore store;

    public FinanceDataInitializer(IDRDataFetcherRegistry registry,
                                  FinanceDataStore store) {
        this.registry = registry;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {
        registry.getAll().forEach((key, fetcher) -> {
            store.put(key, fetcher.fetchAndTransform());
        });
    }
}
