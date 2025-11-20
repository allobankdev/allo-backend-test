package com.nurmaya.allobank.idr_rate_aggregator.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class AggregatedDataStore {
    private final Map<String, List<?>> dataMap = new ConcurrentHashMap<>();
    
    public void putData(String key, List<?> data) {
        if (key == null || data == null) {
            throw new IllegalArgumentException("Key or data cannot be null");
        }

        dataMap.put(key, List.copyOf(data));
    }

    public List<?> getData(String key) {
        return dataMap.get(key);
    }

    public boolean containsKey(String key) {
        return dataMap.containsKey(key);
    }
}
