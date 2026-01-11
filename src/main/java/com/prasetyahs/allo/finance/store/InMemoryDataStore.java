package com.prasetyahs.allo.finance.store;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class InMemoryDataStore {

    private Map<String, Object> data = new HashMap<>();
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public void initialize(Map<String, Object> payload) {
        if (initialized.getAndSet(true)) {
            throw new IllegalStateException("Store is already initialized. Data provided at startup only.");
        }
        this.data = Collections.unmodifiableMap(new HashMap<>(payload));
    }

    public Object retrieve(String key) {
        if (!initialized.get()) {
            throw new IllegalStateException("Store not initialized yet.");
        }
        return data.get(key);
    }
}
