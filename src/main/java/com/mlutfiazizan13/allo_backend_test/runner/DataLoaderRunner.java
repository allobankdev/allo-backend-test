package com.mlutfiazizan13.allo_backend_test.runner;

import com.mlutfiazizan13.allo_backend_test.service.IDRDataFetcherFactory;
import com.mlutfiazizan13.allo_backend_test.service.IDRDataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataLoaderRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoaderRunner.class);

    private final IDRDataFetcherFactory fetcherFactory;
    private final IDRDataStore dataStore;

    public DataLoaderRunner(IDRDataFetcherFactory fetcherFactory, IDRDataStore dataStore) {
        this.fetcherFactory = fetcherFactory;
        this.dataStore = dataStore;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting data load from Frankfurter API...");
        Map<String, Object> data = new HashMap<>();

        for (String strategyType : fetcherFactory.getAvailableStrategies()) {
            try {
                log.info("Fetching resource: {}", strategyType);
                Object result = fetcherFactory.getStrategy(strategyType).fetchData();
                data.put(strategyType, result);
                log.info("Successfully loaded: {}", strategyType);
            } catch (Exception ex) {
                log.error("Failed to load resource '{}': {}",
                        strategyType, ex.getMessage(), ex);
            }
        }

        dataStore.loadData(data);
        log.info("Data loading complete. Loaded {} resources: {}",
                data.size(), data.keySet());
    }
}
