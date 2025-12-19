package com.zultest.allobank_backend_test.store;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryStore {

    private Map<String, Object> store;

    public void put(String key, Object value) {
        if (store != null) {
            throw new IllegalStateException("Store is already initialized");
        }

        store = new HashMap<>();
        store.put(key, value);
    }

    public void markInitialized() {
        store = Collections.unmodifiableMap(store);
    }

    public Object get(String key) {
        Object value = store.get(key);
        if (value == null) {
            throw new IllegalArgumentException("No data found for resourceType: " + key);
        }
        return value;
    }
}
