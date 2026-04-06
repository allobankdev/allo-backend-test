package com.example.allotest.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class DataStoreService {
    
    private final Map<String, Object> store = new ConcurrentHashMap<>();

    public void save(String key, Object value) {
        store.put(key, value);
    }

    public Object get(String key) {
        Object data = store.get(key);
        if(data == null) {
            throw new RuntimeException("Data not found for key: " + key);
        }
        return data;
    }

}
