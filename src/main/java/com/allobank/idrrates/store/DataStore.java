package com.allobank.idrrates.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataStore {

    private static final Logger log = LoggerFactory.getLogger(DataStore.class);
    private final Map<String, Object> store = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    public void put(String resourceType, Object data) {
        if (initialized) {
            throw new IllegalStateException("Data store is already initialized and is immutable");
        }
        log.info("Storing data for resource type: {}", resourceType);
        store.put(resourceType, data);
    }

    public void markInitialized() {
        this.initialized = true;
        log.info("FinanceDataStore initialized with {} resources", store.size());
    }

    public Object get(String resourceType) {
        return store.get(resourceType);
    }

    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(store);
    }
}
