package com.bank.allo.store;

import com.bank.allo.repository.inbound.DataStore;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDataStoreImpl implements DataStore {

    private final Map<String, Object> store = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    @Override
    public synchronized void initialize(Map<String, Object> data) {
        if (initialized) {
            throw new IllegalStateException("Data store already initialized");
        }
        store.putAll(data);
        initialized = true;
    }

    @Override
    public Object get(String key) {
        return store.get(key);
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    public Map<String, Object> asReadOnly() {
        return Collections.unmodifiableMap(store);
    }
}
