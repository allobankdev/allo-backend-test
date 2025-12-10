package com.allo.backend.service;

import com.allo.backend.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class InMemoryDataStore {

    private final Map<String, Object> dataStore = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    public void store(String resourceType, Object data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot store null data");
        }
        log.info("Storing data for resource type: {}", resourceType);
        dataStore.put(resourceType, data);
    }

    public Object retrieve(String resourceType) {
        if (!initialized) {
            throw new IllegalStateException("Data store has not been initialized yet");
        }

        Object data = dataStore.get(resourceType);
        if (data == null) {
            log.error("Resource type not found: {}", resourceType);
            throw new ResourceNotFoundException("Resource type '" + resourceType + "' not found. " +
                    "Available types: latest_idr_rates, historical_idr_usd, supported_currencies");
        }

        log.debug("Retrieved data for resource type: {}", resourceType);
        return data;
    }

    public Map<String, Object> getAllData() {
        return Collections.unmodifiableMap(dataStore);
    }

    public void markAsInitialized() {
        this.initialized = true;
        log.info("Data store marked as initialized with {} resource types", dataStore.size());
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void clear() {
        dataStore.clear();
        initialized = false;
        log.info("Data store cleared");
    }
}
