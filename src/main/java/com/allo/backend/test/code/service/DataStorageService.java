package com.allo.backend.test.code.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread-safe, immutable in-memory data storage service.
 * Data is loaded once on startup and cannot be modified afterward.
 */
@Slf4j
@Service
public class DataStorageService {

    private volatile Map<String, Object> dataStore = new ConcurrentHashMap<>();
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    /**
     * Stores data for a specific resource type.
     * Can only be called before initialization is marked complete.
     */
    public void storeData(String resourceType, Object data) {
        if (initialized.get()) {
            throw new IllegalStateException("Data store is already initialized and immutable");
        }
        dataStore.put(resourceType, data);
        log.debug("Stored data for resource type: {}", resourceType);
    }

    /**
     * Marks the data store as initialized and makes it immutable.
     * After this call, no more data can be added.
     */
    public void markAsInitialized() {
        if (initialized.compareAndSet(false, true)) {
            // Make the map immutable
            dataStore = Collections.unmodifiableMap(new ConcurrentHashMap<>(dataStore));
            log.info("Data store marked as initialized and immutable. Contains {} resources", dataStore.size());
        } else {
            throw new IllegalStateException("Data store is already initialized");
        }
    }

    /**
     * Retrieves data for a specific resource type.
     * Can only be called after initialization is complete.
     */
    public Object getData(String resourceType) {
        if (!initialized.get()) {
            throw new IllegalStateException("Data store is not yet initialized");
        }

        Object data = dataStore.get(resourceType);
        if (data == null) {
            throw new IllegalArgumentException("Unknown resource type: " + resourceType);
        }

        return data;
    }

    /**
     * Checks if the data store has been initialized.
     */
    public boolean isInitialized() {
        return initialized.get();
    }
}
