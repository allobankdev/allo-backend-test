package com.allobank.services;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataCacheService {

   
    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    private volatile boolean ready = false;

    public void put(String key, Object value) {
        cache.put(key, value);
    }

    public Object get(String key) {
        return cache.get(key);
    }

    public void markReady() {
        this.ready = true;
    }

    public boolean isReady() {
        return ready;
    }
}