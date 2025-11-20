package com.example.allow.service;


import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataAggregationService {

    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    private volatile boolean loaded = false;

    public void put(String key, Object data) {
        if (loaded) throw new IllegalStateException("Data already finalized");
        cache.put(key, data);
    }

    public Object get(String key) {
        if (!loaded) throw new IllegalStateException("Data not yet loaded");
        return cache.get(key);
    }

    public void markAsLoaded() {
        this.loaded = true;
    }

    public boolean isLoaded() {
        return loaded;
    }
}
