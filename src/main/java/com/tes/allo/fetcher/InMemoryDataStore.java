package com.tes.allo.fetcher;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

@Service
public class InMemoryDataStore {
    private final AtomicReference<Map<String, Object>> store = new AtomicReference<>(Collections.emptyMap());
    private final AtomicReference<Boolean> loaded = new AtomicReference<>(false);

    public void setAll(Map<String, Object> data) {
        store.set(Collections.unmodifiableMap(data));
        loaded.set(true);
    }

    public boolean isLoaded() { return loaded.get(); }

    public Object get(String key) {
        return store.get().get(key);
    }

    public Map<String, Object> getAll() { return store.get(); }
}
