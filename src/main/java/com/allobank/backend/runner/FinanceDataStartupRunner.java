package com.allobank.backend.runner;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.allobank.backend.store.FinanceDataStore;
import com.allobank.backend.strategy.FinanceDataStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinanceDataStartupRunner implements ApplicationRunner {

    private final Map<String, FinanceDataStrategy> strategies;
    private final FinanceDataStore dataStore;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting data fetching process from Frankfurter API...");
        Map<String, JsonNode> fetchedData = new ConcurrentHashMap<>();

        strategies.forEach((resourceType, strategy) -> {
            try {
            
                JsonNode result = strategy.fetchAndTransformData().block();
                fetchedData.put(resourceType, result);
                log.info("Successfully loaded data for: {}", resourceType);
            } catch (Exception e) {
                log.error("Failed to load data for {}: {}", resourceType, e.getMessage());
            }
        });

        dataStore.initializeStore(fetchedData);
        log.info("All data has been loaded into the store and is ready to serve requests.");
    }
}
