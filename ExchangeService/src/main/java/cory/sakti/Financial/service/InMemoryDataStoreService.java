package cory.sakti.Financial.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryDataStoreService {
    private final Map<String, Object> storage = new HashMap<>(); // Standard HashMap (not thread-safe)
    private boolean initialized = false;

    public void put(String key, Object value) {
        if (initialized) {
            throw new IllegalStateException("Store is sealed. Cannot modify data after startup.");
        }
        storage.put(key, value);
    }

    public Object get(String key) {
        return storage.get(key);
    }

    public void markInitialized() {
        this.initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
