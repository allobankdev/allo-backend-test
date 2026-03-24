package com.allo.test.service;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryDataStore {

    private final Map<String, List<Object>> store = new ConcurrentHashMap<>();

    public void put(String key, List<Object> data) {
        store.put(key, Collections.unmodifiableList(data));
    }

    public List<Object> get(String key) {
        return store.get(key);
    }

    public Map<String, List<Object>> getAll() {
        return Collections.unmodifiableMap(store);
    }
}