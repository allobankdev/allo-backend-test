package com.example.financedata.store;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * Immutable after loadComplete() returns true. Thread-safe reads.
 */
@Component
public class ImmutableFinanceStore {

    private final ConcurrentHashMap<String, Object> internal = new ConcurrentHashMap<>();
    private final AtomicBoolean loaded = new AtomicBoolean(false);

    public void put(String key, Object value) {
        if (loaded.get()) {
            throw new IllegalStateException("Store is immutable after load completed");
        }
        internal.put(key, value);
    }

    public void markLoaded() {
        loaded.set(true);
    }

    public boolean isLoaded() {
        return loaded.get();
    }

    public Map<String, Object> snapshot() {
        // return an unmodifiable shallow copy for safe sharing
        return Collections.unmodifiableMap(new ConcurrentHashMap<>(internal));
    }

    public Object get(String key) {
        return snapshot().get(key);
    }
}
