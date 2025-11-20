package com.allo.idr.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataStoreService {
    private final Map<String, Object> data = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        data.putIfAbsent(key, value);
    }

    public Object get(String key){
        return data.get(key);
    }
}
