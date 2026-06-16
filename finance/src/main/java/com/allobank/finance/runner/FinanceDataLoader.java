package com.allobank.finance.runner;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.allobank.finance.store.FinanceDataStore;
import com.allobank.finance.strategy.IDRDataFetcher;

@Component
public class FinanceDataLoader implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> strategyMap;
    private final FinanceDataStore dataStore;

    public FinanceDataLoader(
            Map<String, IDRDataFetcher> fetchers,
            FinanceDataStore dataStore) {
        this.strategyMap = new HashMap<>();
        fetchers.values()
                .forEach(f -> this.strategyMap.put(f.getResourceType(), f));
        this.dataStore = dataStore;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, Object> loadedData = new HashMap<>();

        strategyMap.forEach((resourceType, strategy) -> {
            System.out.println(">>> Loading data for: " + resourceType);
            Object result = strategy.fetch();
            loadedData.put(resourceType, result);
        });

        dataStore.initialize(loadedData);

        System.out.println(">>> Finance data loaded successfully");
    }
}
