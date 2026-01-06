package com.bezkoder.springjwt.store;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.stereotype.Component;

@Component
public class FinanceDataStore {

    private final AtomicReference<Map<String, List<Object>>> ref =
            new AtomicReference<>(Collections.emptyMap());

    /**
     * Initialize store exactly once. After this, data is immutable.
     */
    public void initialize(Map<String, List<Object>> loadedData) {
        Map<String, List<Object>> immutableCopy = Map.copyOf(loadedData);
        boolean ok = ref.compareAndSet(Collections.emptyMap(), immutableCopy);
        if (!ok) {
            throw new IllegalStateException("FinanceDataStore has already been initialized.");
        }
    }

    public boolean isInitialized() {
        return !ref.get().isEmpty();
    }

    public List<Object> getOrNull(String resourceType) {
        return ref.get().get(resourceType);
    }

    public Map<String, List<Object>> snapshot() {
        return ref.get();
    }
}
