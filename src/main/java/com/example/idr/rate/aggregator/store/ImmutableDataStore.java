package com.example.idr.rate.aggregator.store;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ImmutableDataStore {
    private volatile Map<String, Object> data = Collections.emptyMap();
    private final Object lock = new Object();

    public void setDataIfEmpty(Map<String, Object> newData) {
        synchronized (lock) {
            if (this.data.isEmpty()) {
                this.data = Collections.unmodifiableMap(new ConcurrentHashMap<>(newData));
            } else {
                throw new IllegalStateException("Data store already initialized");
            }
        }
    }
    public Object get(String resourceType) {
        return data.get(resourceType);
    }

    public Map<String, Object> getAll() {
        return data;
    }
}
