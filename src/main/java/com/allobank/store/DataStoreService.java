package com.allobank.store;

import com.allobank.enums.ResourceType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataStoreService {

    private final Map<String, Object> dataStore = new ConcurrentHashMap<>();
    /**
     * -- GETTER --
     *  Checks if the data store is initialized
     */
    @Getter
    private volatile boolean initialized = false;

    /**
     * Stores data for a specific resource type
     * Should only be called during application startup
     * Note: No synchronized needed as this is only called from single-threaded ApplicationRunner
     */
    public void storeData(ResourceType resourceType, Object data) {
        if (initialized) {
            log.warn("Attempting to modify data store after initialization");
            throw new IllegalStateException("Data store is already initialized and immutable");
        }

        String key = resourceType.getValue();
        dataStore.put(key, data);
        log.info("Stored data for resource type: {}", key);
    }

    /**
     * Marks the data store as initialized and immutable
     * Note: No synchronized needed as this is only called once from ApplicationRunner
     */
    public void markInitialized() {
        if (!initialized) {
            initialized = true;
            log.info("Data store initialized with {} resources", dataStore.size());
        }
    }

    /**
     * Retrieves data for a specific resource type
     * Returns immutable view to prevent external modification
     */
    public Object getData(String resourceType) {
        if (!initialized) {
            throw new IllegalStateException("Data store not yet initialized");
        }

        Object data = dataStore.get(resourceType);
        if (data == null) {
            throw new IllegalArgumentException("Unknown resource type: " + resourceType);
        }

        return data;
    }

    /**
     * Returns all stored data as an unmodifiable map
     */
    public Map<String, Object> getAllData() {
        if (!initialized) {
            throw new IllegalStateException("Data store not yet initialized");
        }

        return Collections.unmodifiableMap(dataStore);
    }

}
