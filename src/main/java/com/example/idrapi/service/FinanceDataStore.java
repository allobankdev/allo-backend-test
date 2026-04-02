package com.example.idrapi.service;

import com.example.idrapi.model.FinanceDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class FinanceDataStore {

    private final Map<String, FinanceDataResponse> store = new ConcurrentHashMap<>();

    private volatile boolean sealed = false;

    public void put(String resourceType, FinanceDataResponse response) {
        if (sealed) {
            log.warn("Attempted to write to sealed FinanceDataStore for key '{}' — ignored.", resourceType);
            return;
        }
        store.put(resourceType, response);
        log.info("Stored data for resourceType: '{}'", resourceType);
    }

    public void seal() {
        this.sealed = true;
        log.info("FinanceDataStore sealed. Loaded resources: {}", store.keySet());
    }

    public Optional<FinanceDataResponse> get(String resourceType) {
        return Optional.ofNullable(store.get(resourceType));
    }

    public Map<String, FinanceDataResponse> getAll() {
        return Collections.unmodifiableMap(store);
    }

    public boolean isSealed() {
        return sealed;
    }
}
