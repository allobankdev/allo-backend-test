package com.allobank.service;

import com.allobank.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service for coordinating data fetching and storage using the Strategy Pattern.
 * Maintains a map of strategies for dynamic selection based on resource type.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataFetchingService {
    
    private final DataStore dataStore;
    private final List<IDRDataFetcher> strategies;
    private Map<String, IDRDataFetcher> strategyMap;
    
    /**
     * Initialize the strategy map from the list of injected strategies.
     * This is called automatically after beans are constructed.
     */
    public void initializeStrategyMap() {
        strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        IDRDataFetcher::getResourceType,
                        Function.identity()
                ));
        
        log.info("Initialized strategy map with {} strategies: {}", 
                strategyMap.size(), strategyMap.keySet());
    }
    
    /**
     * Fetch and store data for a specific resource type.
     * Uses the Strategy Pattern to select the appropriate fetcher.
     * 
     * @param resourceType the resource type identifier
     * @return true if successful, false otherwise
     */
    public boolean fetchAndStoreData(String resourceType) {
        if (strategyMap == null) {
            log.error("Strategy map not initialized. Call initializeStrategyMap() first.");
            return false;
        }
        
        IDRDataFetcher fetcher = strategyMap.get(resourceType);
        if (fetcher == null) {
            log.error("No strategy found for resource type: {}", resourceType);
            return false;
        }
        
        try {
            log.info("Fetching data for resource type: {}", resourceType);
            Object data = fetcher.fetchData();
            boolean stored = dataStore.storeData(resourceType, data);
            
            if (stored) {
                log.info("Successfully fetched and stored data for: {}", resourceType);
                return true;
            } else {
                log.warn("Failed to store data for {}: store may be finalized", resourceType);
                return false;
            }
        } catch (Exception e) {
            log.error("Error fetching data for resource type: {}", resourceType, e);
            return false;
        }
    }
    
    /**
     * Fetch and store data for all configured strategies.
     * 
     * @return the number of successfully loaded resources
     */
    public int fetchAllData() {
        if (strategyMap == null) {
            initializeStrategyMap();
        }
        
        int successCount = 0;
        for (String resourceType : strategyMap.keySet()) {
            if (fetchAndStoreData(resourceType)) {
                successCount++;
            }
        }
        
        return successCount;
    }
    
    /**
     * Get data for a specific resource type from the store.
     * 
     * @param resourceType the resource type identifier
     * @return the stored data, or null if not found
     */
    public Object getStoredData(String resourceType) {
        return dataStore.getData(resourceType);
    }
    
    /**
     * Check if the data store is initialized.
     * 
     * @return true if initialized, false otherwise
     */
    public boolean isDataStoreInitialized() {
        return dataStore.isInitialized();
    }
}
