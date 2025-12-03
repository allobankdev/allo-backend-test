package com.example.frankfurter.runner;

import com.example.frankfurter.store.FinanceDataStore;
import com.example.frankfurter.strategy.IDRDataFetcher;
import com.example.frankfurter.strategy.IDRDataFetcherRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FinanceDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(FinanceDataInitializer.class);

    private final IDRDataFetcherRegistry registry;
    private final FinanceDataStore store;

    public FinanceDataInitializer(IDRDataFetcherRegistry registry, FinanceDataStore store) {
        this.registry = registry;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Initializing FinanceDataStore with all resource types...");
        Map<String, List<?>> initialData = new HashMap<>();

        registry.getSupportedTypes().forEach(type -> {
            IDRDataFetcher fetcher = registry.getFetcher(type);
            List<?> data = fetcher.fetchData();
            initialData.put(type, data);
            log.info("Loaded {} items for resourceType={}", data.size(), type);
        });

        store.initialize(initialData);
        log.info("FinanceDataStore initialization completed.");
    }
}
