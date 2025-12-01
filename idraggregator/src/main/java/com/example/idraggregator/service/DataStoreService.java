package com.example.idraggregator.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Holds immutable in-memory data loaded at startup.
 * Thread-safe: after initial population, the internal map is unmodifiable.
 */
@Service
public class DataStoreService {

    private final AtomicReference<Map<String, Object>> storeRef = new AtomicReference<>(Collections.emptyMap());
    private final AtomicReference<Boolean> initialized = new AtomicReference<>(false);

    public void setAll(Map<String, Object> allData) {
        Objects.requireNonNull(allData);
        if (!initialized.compareAndSet(false, true)) {
            throw new IllegalStateException("Data store already initialized and is immutable");
        }
        // wrap with unmodifiable map
        this.storeRef.set(Collections.unmodifiableMap(allData));
    }

    public Object get(String key) {
        return storeRef.get().get(key);
    }

    public Map<String, Object> getAll() {
        return storeRef.get();
    }

    public boolean isInitialized() {
        return initialized.get();
    }
}
