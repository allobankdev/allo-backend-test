package com.example.idr.service.store;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class FinanceDataStore {

    private final Map<String, List<?>> store =
            new ConcurrentHashMap<>();

    public void put(String key, List<?> data) {
        store.put(key, List.copyOf(data));
    }

    public List<?> get(String key) {
        return store.getOrDefault(key, List.of());
    }
}
