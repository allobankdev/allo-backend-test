package com.allobanktest.idr.store;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class DataStore {
    private final AtomicReference<Map<String, Map<String, Object>>> ref = new AtomicReference<>();

    public void loadInitialData(Map<String, Map<String, Object>> data) {
        if (!ref.compareAndSet(null, Map.copyOf(data))) {
            throw new IllegalStateException("DataStore already initialized");
        }
    }

    public Map<String, Map<String, Object>> getAll() {
        var m = ref.get();
        if (m == null) throw new IllegalStateException("Data not loaded yet");
        return m;
    }

    public Map<String, Object> get(String key) {
        var m = getAll();
        return m.get(key);
    }
}
