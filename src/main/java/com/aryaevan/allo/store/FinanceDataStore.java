package com.aryaevan.allo.store;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Thread-safe, immutable in-memory store for aggregated finance data.
 * Once initialized, the data cannot be modified, ensuring thread safety without locks.
 */
public class FinanceDataStore {
    
    private volatile Map<String, Object> data;
    private volatile boolean initialized = false;
    
    /**
     * Initializes the store with aggregated data from all three resources.
     * This should only be called once during application startup.
     * Makes the data immutable using Map.copyOf().
     * 
     * @param loadedData The aggregated data from all resources
     */
    public synchronized void initialize(Map<String, Object> loadedData) {
        if (initialized) {
            throw new IllegalStateException("Data store is already initialized");
        }
        
        // Create an immutable copy of the data
        this.data = Collections.unmodifiableMap(new HashMap<>(loadedData));
        this.initialized = true;
    }
    
    /**
     * Retrieves data for a specific resource type from the immutable store.
     * 
     * @param resourceType The resource type (latest_idr_rates, historical_idr_usd, supported_currencies)
     * @return The cached data for the resource, or null if not found
     * @throws IllegalStateException if store is not yet initialized
     */
    public Object get(String resourceType) {
        if (!initialized) {
            throw new IllegalStateException("Data store is not yet initialized");
        }
        
        return data.get(resourceType);
    }
    
    /**
     * Checks if the store has been initialized.
     * 
     * @return true if initialized, false otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Gets all cached data as an immutable map.
     * 
     * @return Immutable map of all cached data
     */
    public Map<String, Object> getAll() {
        if (!initialized) {
            throw new IllegalStateException("Data store is not yet initialized");
        }
        
        return data;
    }
}
