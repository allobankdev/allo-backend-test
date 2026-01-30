package com.example.demo.store;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FinanceDataStore {

    private final Map<String, List<?>> data = new ConcurrentHashMap<>();

    public void load(String key, List<?> value) {
        data.put(key, List.copyOf(value));
    }

    public List<?> get(String key) {
        return data.get(key);
    }
}
