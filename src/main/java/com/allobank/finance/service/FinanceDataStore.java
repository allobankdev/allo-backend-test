package com.allobank.finance.service;

import com.allobank.finance.dto.FinanceDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
@Component
public class FinanceDataStore {

    private final ConcurrentHashMap<String, FinanceDataResponse> store = new ConcurrentHashMap<>();
    private final AtomicBoolean sealed = new AtomicBoolean(false);

    public void put(String resourceType, FinanceDataResponse response) {
        if (sealed.get()) {
            throw new IllegalStateException(
                "FinanceDataStore is sealed. No writes allowed after application startup.");
        }
        store.put(resourceType, response);
        log.debug("Stored data for resource type: {}", resourceType);
    }

    public void seal() {
        if (sealed.compareAndSet(false, true)) {
            log.info("FinanceDataStore sealed. {} resource(s) loaded: {}",
                    store.size(), store.keySet());
        }
    }

    public FinanceDataResponse get(String resourceType) {
        return store.get(resourceType);
    }

    public Map<String, FinanceDataResponse> getAll() {
        return Collections.unmodifiableMap(store);
    }

    public boolean isSealed() {
        return sealed.get();
    }

    public boolean containsKey(String resourceType) {
        return store.containsKey(resourceType);
    }
}
