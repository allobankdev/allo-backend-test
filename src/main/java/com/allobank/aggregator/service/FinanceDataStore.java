package com.allobank.aggregator.service;

import com.allobank.aggregator.dto.FinanceDataDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class FinanceDataStore {

    // volatile for visibility; replaced once at initialize
    private volatile Map<String, FinanceDataDto> store = Collections.emptyMap();

    /** Initialize once during startup. Throws if initialize is called again. */
    public synchronized void initialize(Map<String, FinanceDataDto> data) {
        if (!this.store.isEmpty()) {
            throw new IllegalStateException("FinanceDataStore already initialized");
        }
        this.store = Collections.unmodifiableMap(data);
    }

    public Optional<FinanceDataDto> get(String resourceType) {
        return Optional.ofNullable(store.get(resourceType));
    }

    public Map<String, FinanceDataDto> all() {
        return store;
    }
}
