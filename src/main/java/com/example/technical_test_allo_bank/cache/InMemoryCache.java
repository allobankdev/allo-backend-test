package com.example.technical_test_allo_bank.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryCache {

    private final Map<String, Object> data = new ConcurrentHashMap<>();

    public Object get(String key) {
        return data.get(key);
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }
}
