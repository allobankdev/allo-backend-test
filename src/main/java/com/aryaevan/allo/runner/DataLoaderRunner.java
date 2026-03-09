package com.aryaevan.allo.runner;

import com.aryaevan.allo.store.FinanceDataStore;
import com.aryaevan.allo.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * ApplicationRunner component that loads all aggregated finance data on application startup.
 * This ensures all data is fetched exactly once and loaded into the immutable in-memory store
 * before the application is ready to serve requests.
 */
@Component
public class DataLoaderRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataLoaderRunner.class);
    
    private final Map<String, IDRDataFetcher> strategyMap;
    private final FinanceDataStore dataStore;
    
    @Autowired
    public DataLoaderRunner(Map<String, IDRDataFetcher> strategyMap, FinanceDataStore dataStore) {
        this.strategyMap = strategyMap;
        this.dataStore = dataStore;
    }
    
    /**
     * Creates an ApplicationRunner bean that loads all data on startup.
     * 
     * @return ApplicationRunner that executes the data loading logic
     */
    @Bean
    public ApplicationRunner loadDataRunner() {
        return args -> {
            logger.info("Starting data loader runner...");
            
            Map<String, Object> aggregatedData = new HashMap<>();
            
            // Fetch data from all three strategies
            for (Map.Entry<String, IDRDataFetcher> entry : strategyMap.entrySet()) {
                String resourceType = entry.getKey();
                IDRDataFetcher fetcher = entry.getValue();
                
                try {
                    logger.info("Fetching data for resource type: {}", resourceType);
                    Object data = fetcher.fetchData();
                    aggregatedData.put(resourceType, data);
                    logger.info("Successfully fetched data for: {}", resourceType);
                } catch (Exception e) {
                    logger.error("Failed to fetch data for resource type: {}", resourceType, e);
                    throw new RuntimeException("Failed to load data for " + resourceType + " on startup", e);
                }
            }
            
            // Initialize the immutable store with all aggregated data
            dataStore.initialize(aggregatedData);
            logger.info("Data store initialized successfully with {} resources", aggregatedData.size());
        };
    }
}
