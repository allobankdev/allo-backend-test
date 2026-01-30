package com.example.allobank.store;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FinanceDataStore {

    private final Map<String, List<?>> store = new ConcurrentHashMap<>();

    public void put(String key, List<?> data) {
        store.put(key, List.copyOf(data));
    }

    public List<?> get(String key) {
        return store.getOrDefault(key, List.of());
    }
}