package com.chnh16.backendtest.service;

import com.chnh16.backendtest.model.response.CommonResponse;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class InMemoryStoreService {

    private final ConcurrentMap<String, String> cache = new ConcurrentHashMap<>();

    public String get(String key) {
        return cache.get(key);
    }

    public void put(String key, String value) {
        cache.put(key, value);
    }

    public boolean contains(String key) {
        return cache.containsKey(key);
    }

}
