package com.allobank.allobackendtest.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataStoreService {
    private final Map<String, Object> storage = new ConcurrentHashMap<>();

    public void storeData(String key, Object data){
        storage.put(key, data);
    }

    public Object getData(String key){
        return storage.get(key);
    }
}
