package com.allobank.backend.store;


import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

@Service
public class FinanceDataStore {
    
    private Map<String, JsonNode> immutableDataStore;

    public void initializeStore(Map<String, JsonNode> data) {
        this.immutableDataStore = Map.copyOf(data); 
    }

    public JsonNode getData(String resourceType) {
        if (immutableDataStore == null || !immutableDataStore.containsKey(resourceType)) {
            throw new IllegalArgumentException("Data tidak ditemukan atau belum siap: " + resourceType);
        }
        return immutableDataStore.get(resourceType);
    }
}