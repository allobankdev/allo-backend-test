package com.finance.service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class FinanceCacheService {

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public void put(String key, Object value){
        cache.put(key, value);
    }

    public Object get(String key){
        return cache.get(key);
    }

    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(cache);
    }
}
