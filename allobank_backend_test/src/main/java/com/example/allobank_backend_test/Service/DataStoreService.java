package com.example.allobank_backend_test.Service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DataStoreService {
    private volatile Map<String, Object> data = Map.of();

    public void saveAll(Map<String, Object> newData) {
        this.data = Map.copyOf(newData);
    }

    public Object get(String key) {
        return data.get(key);
    }
}
