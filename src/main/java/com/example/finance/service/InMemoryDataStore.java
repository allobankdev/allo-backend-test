package com.example.finance.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collections;

@Service
public class InMemoryDataStore {

    private final Map<String, Object> dataStore = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        dataStore.put(key, value);
    }

    public Object get(String key) {
        return dataStore.get(key);
    }

    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(dataStore);
    }
}