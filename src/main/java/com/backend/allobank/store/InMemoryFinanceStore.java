package com.backend.allobank.store;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class InMemoryFinanceStore {

    private final AtomicReference<Map<String, Object>> dataRef = new AtomicReference<>(Collections.emptyMap());

    public void initialize(Map<String, Object> newData) {
        Map<String, Object> immutable = Map.copyOf(newData);

        boolean updated = dataRef.compareAndSet(
                dataRef.get(),
                immutable
        );

        if (!updated) {
            throw new IllegalStateException("FinanceStore already initialized");
        }
    }

    public Object get(String resourceType) {
        return dataRef.get().get(resourceType);
    }

    public Map<String, Object> getAll() {
        return dataRef.get();
    }
}
