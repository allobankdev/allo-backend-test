package com.example.finance.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FinanceDataService {
    private final Map<String, List<Map<String, Object>>> store = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    public void setData(String resourceType, List<Map<String, Object>> data) {
        if (initialized) {
            throw new IllegalStateException("Data store already initialized");
        }
        store.put(resourceType, Collections.unmodifiableList(data));
    }

    public List<Map<String, Object>> getData(String resourceType) {
        return store.get(resourceType);
    }

    public void markInitialized() {
        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }
}