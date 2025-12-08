package com.example.allobank.project.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class DataMemoryStore {
    private final Map<String, Object> storeData = new ConcurrentHashMap<>();

    public void put(String key, Object data) {
    	storeData.put(key, data);
    }

    public Object get(String key) {
        return storeData.get(key);
    }

    public Map<String, Object> snapshot() {
        return Map.copyOf(storeData); 
    }

    public Set<String> keys() {
        return storeData.keySet();
    }

    public boolean exists(String key) {
        return storeData.containsKey(key);
    }
}