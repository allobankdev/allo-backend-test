package com.allobank.financeapi.service;

import com.allobank.financeapi.model.enums.ResourceType;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FinanceDataService {

    private Map<ResourceType, Object> dataStore = new ConcurrentHashMap<>();

    public void storeData(ResourceType resourceType, Object data) {
        this.dataStore.put(resourceType, data);
    }

    public Optional<Object> getData(ResourceType resourceType) {
        return Optional.ofNullable(this.dataStore.get(resourceType));
    }

    public void setImmutable() {
        this.dataStore = Collections.unmodifiableMap(new ConcurrentHashMap<>(this.dataStore));
    }

    // Added for testing purposes
    public void clearData() {
        this.dataStore = new ConcurrentHashMap<>();
    }
}
