package com.allo.backend.runner;

import com.allo.backend.exception.DataInitializationException;
import com.allo.backend.service.FinanceDataService;
import com.allo.backend.service.InMemoryDataStore;
import com.allo.backend.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializationRunner implements ApplicationRunner {

    private final FinanceDataService financeDataService;
    private final InMemoryDataStore dataStore;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting data initialization on application startup...");

        try {
            Map<String, IDRDataFetcher> strategies = financeDataService.getAllStrategies();

            if (strategies.isEmpty()) {
                throw new DataInitializationException("No data fetching strategies found");
            }

            log.info("Fetching data for {} resource types", strategies.size());

            for (Map.Entry<String, IDRDataFetcher> entry : strategies.entrySet()) {
                String resourceType = entry.getKey();
                IDRDataFetcher strategy = entry.getValue();

                try {
                    log.info("Fetching data for resource type: {}", resourceType);
                    Object data = strategy.fetchData();
                    dataStore.store(resourceType, data);
                    log.info("Successfully loaded data for resource type: {}", resourceType);
                } catch (Exception e) {
                    log.error("Failed to load data for resource type: {}", resourceType, e);
                    throw new DataInitializationException(
                            "Failed to initialize data for resource type: " + resourceType, e);
                }
            }

            dataStore.markAsInitialized();
            log.info("Data initialization completed successfully. All {} resources loaded.", strategies.size());

        } catch (Exception e) {
            log.error("Critical error during data initialization", e);
            throw new DataInitializationException("Application startup failed due to data initialization error", e);
        }
    }
}
