package com.allobank.frankfurter_aggregator.service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataStorageService {
    
    @Getter
    private final Map<String, Object> dataStore = new ConcurrentHashMap<>();
    
    private volatile boolean isLoaded = false;
    
    public void storeData(String resourceType, Object data) {
        dataStore.put(resourceType, data);
        log.info("Stored data for resource type: {}", resourceType);
    }
    
    public Object getData(String resourceType) {
        if (!isLoaded) {
            throw new IllegalStateException("Data not loaded yet. Application is still starting up.");
        }
        return dataStore.get(resourceType);
    }
    
    public void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
        if (loaded) {
            log.info("All data has been loaded successfully. Data store size: {}", dataStore.size());
        }
    }
    
    public boolean isLoaded() {
        return isLoaded;
    }
    
    public Map<String, Object> getAllData() {
        return Collections.unmodifiableMap(dataStore);
    }
}
