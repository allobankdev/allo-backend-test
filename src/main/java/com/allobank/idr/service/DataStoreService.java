package com.allobank.idr.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataStoreService {
    
    private final Map<String, Map<String, Object>> dataStore = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    public void storeData(String resourceType, Map<String, Object> data) {
        if (!initialized) {
            dataStore.put(resourceType, Collections.unmodifiableMap(data));
        }
    }

    public Map<String, Object> getData(String resourceType) {
        return dataStore.get(resourceType);
    }

    public void markAsInitialized() {
        this.initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
