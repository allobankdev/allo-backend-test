package com.allobank.test.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory thread-safe store for holding the pre-fetched financial data.
 */
@Service
public class DataStoreService {
    
    // ConcurrentHashMap ensures thread-safety. Object holds the transformed JSON data.
    private final Map<String, Object> memoryStore = new ConcurrentHashMap<>();

    public void storeData(String resourceType, Object data) {
        memoryStore.put(resourceType, data);
    }

    public Object retrieveData(String resourceType) {
        return memoryStore.get(resourceType);
    }
    
    public Map<String, Object> getAllData() {
        return Collections.unmodifiableMap(memoryStore);
    }
}
