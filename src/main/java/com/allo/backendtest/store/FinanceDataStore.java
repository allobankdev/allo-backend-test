package com.allo.backendtest.store;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FinanceDataStore {

    private final Map<String, List<Object>> internalStore = new ConcurrentHashMap<>();
    private volatile Map<String, List<Object>> snapshot;

    public void put(String key, List<Object> data) {
        internalStore.put(key, List.copyOf(data));
    }

    public void finalizeSnapshot() {
        snapshot = Collections.unmodifiableMap(internalStore);
    }

    public List<Object> get(String key) {
        if (snapshot == null) {
            throw new IllegalStateException("Data not initialized yet.");
        }
        return snapshot.getOrDefault(key, List.of());
    }
    public boolean contains(String key) {
        return snapshot != null && snapshot.containsKey(key);
    }
}
