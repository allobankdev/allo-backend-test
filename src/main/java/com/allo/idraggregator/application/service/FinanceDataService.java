package com.allo.idraggregator.application.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.allo.idraggregator.presentation.exception.ResourceTypeNotFoundException;

@Service
public class FinanceDataService {

    private final Map<String, Object> storage = new ConcurrentHashMap<>();

    public void store(String type, Object data) {

        storage.put(type, data);
    }

    public Object get(String type) {

        if (!storage.containsKey(type)) {
            throw new ResourceTypeNotFoundException(type);
        }

        return storage.getOrDefault(type, null);
        
    }
}
