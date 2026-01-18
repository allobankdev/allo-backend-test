package com.example.allobank.backend.test.takehometest.store;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class DataStore {

    private final Map<String, List<?>> data = new ConcurrentHashMap<>();

    public void put(String key, List<Object> value) {
        data.put(key, List.copyOf(value));
    }

    public List<?> get(String key) {
        return data.getOrDefault(key, List.of());
    }
}
