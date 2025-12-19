package com.zultest.allobank_backend_test.store;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryStore {

    private final Map<String, Object> dataStore = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    public void put(String resourceType, Object data) {
        if (initialized) {
            throw new IllegalStateException("Store is already initialized and immutable");
        }
        dataStore.put(resourceType, data);
    }

    public void markInitialized() {
        this.initialized = true;
    }

    public Object get(String resourceType) {
        Object data = dataStore.get(resourceType);
        if (data == null) {
            throw new IllegalArgumentException(
                    "No data found for resourceType: " + resourceType
            );
        }
        return data;
    }

    public Map<String, Object> snapshot() {
        return Collections.unmodifiableMap(dataStore);
    }
}
