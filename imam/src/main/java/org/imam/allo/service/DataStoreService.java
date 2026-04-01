package org.imam.allo.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataStoreService {
    private final Map<String, Object> store = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        store.put(key, value);
    }

    public Object get(String key) {
        Object data = store.get(key);

        if (data == null) {
            throw new IllegalArgumentException("Resource not found: " + key);
        }

        return data;
    }
}
