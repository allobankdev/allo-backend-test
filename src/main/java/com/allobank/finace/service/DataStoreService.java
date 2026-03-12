package com.allobank.finace.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DataStoreService {

    private final ConcurrentHashMap<String, List<Map<String, Object>>> store = new ConcurrentHashMap<>();
    private boolean initialized = false;

    public synchronized void store(String resourceType, List<Map<String, Object>> data) {
        if (initialized) {
            throw new IllegalStateException("Data store is already initialized and is immutable.");
        }
        store.put(resourceType, Collections.unmodifiableList(data));
        log.info("Stored {} records for resource type '{}'", data.size(), resourceType);
    }

    public synchronized void markInitialized() {
        this.initialized = true;
        log.info("Data store marked as initialized. No further writes allowed.");
    }

    public List<Map<String, Object>> get(String resourceType) {
        return store.get(resourceType);
    }

    public boolean contains(String resourceType) {
        return store.containsKey(resourceType);
    }
}
