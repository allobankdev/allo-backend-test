package com.sdewa.IdrRateAggregator.services.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.sdewa.IdrRateAggregator.services.AppDataStore;

@Service
public class AppDataStoreImpl implements AppDataStore {

    private final Map<String, Object> store = new ConcurrentHashMap<>();

    @Override
    public void put(String key, Object value) {
        store.put(key, value);
    }

    @Override
    public Object get(String key) {
        return store.get(key);
    }

}
