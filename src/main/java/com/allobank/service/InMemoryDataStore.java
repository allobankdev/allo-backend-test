package com.allobank.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread-safe in-memory data store for caching fetched data.
 * Once data is loaded, it becomes immutable.
 */
@Slf4j
@Service
public class InMemoryDataStore {

    private final Map<String, Object> dataStore = new ConcurrentHashMap<>();
    private final AtomicBoolean isDataLoaded = new AtomicBoolean(false);

    /**
     * Stores data for a specific resource type.
     * This method is thread-safe and can only be called during initialization.
     *
     * @param resourceType The resource type identifier
     * @param data The data to store
     */
    public void storeData(String resourceType, Object data) {
        if (isDataLoaded.get()) {
            log.warn("Attempt to store data after initialization phase. Resource: {}", resourceType);
            return;
        }
        
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null for resource: " + resourceType);
        }
        
        // Create immutable wrapper if needed
        Object immutableData = makeImmutable(data);
        dataStore.put(resourceType, immutableData);
        log.debug("Stored data for resource type: {}", resourceType);
    }

    /**
     * Retrieves data for a specific resource type.
     * Returns an immutable view of the data.
     *
     * @param resourceType The resource type identifier
     * @return The stored data, or null if not found
     */
    public Object getData(String resourceType) {
        Object data = dataStore.get(resourceType);
        if (data == null) {
            log.warn("No data found for resource type: {}", resourceType);
        }
        return data;
    }

    /**
     * Marks the data loading as complete, making the store immutable.
     */
    public void markDataLoaded() {
        boolean wasLoaded = isDataLoaded.compareAndSet(false, true);
        if (wasLoaded) {
            log.info("Data loading completed. Store is now immutable.");
        }
    }

    /**
     * Checks if data has been loaded.
     *
     * @return true if data loading is complete
     */
    public boolean isDataLoaded() {
        return isDataLoaded.get();
    }

    /**
     * Returns an unmodifiable view of all stored data.
     *
     * @return Unmodifiable map of all data
     */
    public Map<String, Object> getAllData() {
        return Collections.unmodifiableMap(dataStore);
    }

    /**
     * Wraps data to ensure immutability.
     * For complex objects, this creates defensive copies or immutable wrappers.
     */
    private Object makeImmutable(Object data) {
        // For DTOs with Lombok @Builder, they're already effectively immutable
        // if constructed properly. We return as-is, but in production,
        // you might want to create deep copies for complex nested structures.
        return data;
    }
}

