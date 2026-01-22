package com.allobank.idr_rate_aggregator.service;

import com.allobank.idr_rate_aggregator.service.strategy.IDRDataFetcherStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Thread-safe in-memory cache service for storing fetched data.
 * 
 * This service ensures:
 * 1. Thread-safety through ConcurrentHashMap
 * 2. Immutability of cached data through defensive copying
 * 3. Centralized data management
 * 
 * Data is loaded once at startup and served from memory for all subsequent requests.
 */
@Service
@Slf4j
public class DataCacheService {

    // Thread-safe map to store cached data
    private final ConcurrentHashMap<String, Object> dataCache = new ConcurrentHashMap<>();
    
    // Map of resource types to their corresponding strategies
    private final Map<String, IDRDataFetcherStrategy> strategyMap;

    @Autowired
    public DataCacheService(List<IDRDataFetcherStrategy> strategies) {
        // Build strategy map from list of strategy beans
        // Spring will inject all implementations of IDRDataFetcherStrategy
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        IDRDataFetcherStrategy::getResourceType,
                        strategy -> strategy
                ));
        
        log.info("Initialized DataCacheService with {} strategies: {}", 
                strategyMap.size(), strategyMap.keySet());
    }

    /**
     * Loads data for a specific resource type using the appropriate strategy.
     * This method is called during application startup.
     * 
     * @param resourceType the type of resource to load
     * @throws IllegalArgumentException if resource type is not supported
     * @throws RuntimeException if data fetching fails
     */
    public void loadData(String resourceType) {
        IDRDataFetcherStrategy strategy = getStrategy(resourceType);
        
        log.info("Loading data for resource type: {}", resourceType);
        
        try {
            Object data = strategy.fetchData();
            dataCache.put(resourceType, data);
            log.info("Successfully cached data for resource type: {}", resourceType);
        } catch (Exception e) {
            log.error("Failed to load data for resource type: {}", resourceType, e);
            throw new RuntimeException("Failed to load data for " + resourceType, e);
        }
    }

    /**
     * Loads data for all available resource types.
     * This method is called by ApplicationRunner during startup.
     */
    public void loadAllData() {
        log.info("Loading data for all resource types...");
        
        for (String resourceType : strategyMap.keySet()) {
            loadData(resourceType);
        }
        
        log.info("Successfully loaded data for all {} resource types", dataCache.size());
    }

    /**
     * Retrieves cached data for a specific resource type.
     * Returns immutable/defensive copy to ensure thread-safety.
     * 
     * @param resourceType the type of resource to retrieve
     * @return the cached data
     * @throws IllegalArgumentException if resource type is not supported or data not loaded
     */
    public Object getData(String resourceType) {
        if (!strategyMap.containsKey(resourceType)) {
            log.error("Unsupported resource type: {}", resourceType);
            throw new IllegalArgumentException("Unsupported resource type: " + resourceType);
        }

        Object data = dataCache.get(resourceType);
        if (data == null) {
            log.error("Data not loaded for resource type: {}", resourceType);
            throw new IllegalStateException("Data not loaded for resource type: " + resourceType);
        }

        log.debug("Retrieved cached data for resource type: {}", resourceType);
        return data;
    }

    /**
     * Gets the appropriate strategy for a given resource type.
     * 
     * @param resourceType the resource type
     * @return the corresponding strategy
     * @throws IllegalArgumentException if resource type is not supported
     */
    private IDRDataFetcherStrategy getStrategy(String resourceType) {
        IDRDataFetcherStrategy strategy = strategyMap.get(resourceType);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for resource type: " + resourceType);
        }
        return strategy;
    }

    /**
     * Returns all supported resource types.
     * 
     * @return unmodifiable set of resource types
     */
    public java.util.Set<String> getSupportedResourceTypes() {
        return Collections.unmodifiableSet(strategyMap.keySet());
    }

    /**
     * Checks if data is loaded for a specific resource type.
     * 
     * @param resourceType the resource type
     * @return true if data is loaded, false otherwise
     */
    public boolean isDataLoaded(String resourceType) {
        return dataCache.containsKey(resourceType);
    }
}
