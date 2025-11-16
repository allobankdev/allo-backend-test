package com.allobank.allobackendtest.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class InMemoryFinanceStore {

    private final AtomicReference<Map<String, Object>> dataRef =
            new AtomicReference<>(Map.of());

    /** Dipanggil sekali saat startup oleh ApplicationRunner */
    public void initialize(Map<String, Object> initialData) {
        // buat immutable copy
        this.dataRef.set(Map.copyOf(initialData));
    }

    public Object getByResourceType(String resourceType) {
        Object result = dataRef.get().get(resourceType);
        if (result == null) {
            throw new IllegalArgumentException("Unknown resource type: " + resourceType);
        }
        return result;
    }
}
