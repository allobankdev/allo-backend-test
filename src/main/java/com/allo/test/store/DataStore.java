package com.allo.test.store;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataStore {

    private final Map<String, List<?>> store = new ConcurrentHashMap<>();

    public void put(String key, List<?> data) {
        // immutable list
        store.put(key, List.copyOf(data));
    }

    public List<?> get(String key) {
        return store.getOrDefault(key, List.of());
    }
}