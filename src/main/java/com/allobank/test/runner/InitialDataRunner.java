package com.allobank.test.runner;

import com.allobank.test.service.DataStoreService;
import com.allobank.test.strategy.DataFetcherStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Executes immediately after context starts to fetch target resources 
 * using all defined strategies and caches their immutable values.
 */
@Component
public class InitialDataRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(InitialDataRunner.class);
    
    private final List<DataFetcherStrategy> fetcherStrategies;
    private final DataStoreService dataStoreService;

    @Autowired
    public InitialDataRunner(List<DataFetcherStrategy> fetcherStrategies, 
                             DataStoreService dataStoreService) {
        this.fetcherStrategies = fetcherStrategies;
        this.dataStoreService = dataStoreService;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting initial data ingestion from Frankfurter API...");
        
        for (DataFetcherStrategy strategy : fetcherStrategies) {
            String resourceType = strategy.getResourceType();
            try {
                log.info("Fetching data for resource: {}", resourceType);
                Object transformedData = strategy.fetchAndTransform();
                
                // Save it into the memory service to adhere to immutability Constraint C
                dataStoreService.storeData(resourceType, transformedData);
                
                log.info("Successfully fetched and cached resource: {}", resourceType);
            } catch (Exception e) {
                log.error("Failed to load and transform data for resource: {} - {}", resourceType, e.getMessage(), e);
            }
        }
        
        log.info("Initial data startup load procedure completed.");
    }
}
