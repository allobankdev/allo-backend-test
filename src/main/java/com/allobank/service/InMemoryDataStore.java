package com.allobank.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryDataStore {
    private final Map<String, Object> store = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        store.put(key, value);
    }

    public Object get(String key) {
        return store.get(key);
    }

    public void makeImmutable() {
        store.replaceAll((k, v) -> {
            if (v instanceof Map<?, ?> map) {
                return Collections.unmodifiableMap(map);
            }
            return v;
        });
    }
}
