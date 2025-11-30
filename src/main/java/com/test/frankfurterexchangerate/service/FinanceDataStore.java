package com.test.frankfurterexchangerate.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FinanceDataStore {
    private final Map<String, List<Object>> store = new ConcurrentHashMap<>();

    public void save(String key, List<Object> data){
        store.put(key, data);
    }

    public List<Object> get(String key){
        return store.get(key);
    }
}
