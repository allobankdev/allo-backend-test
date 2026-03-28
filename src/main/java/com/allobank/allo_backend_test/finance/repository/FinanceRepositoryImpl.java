package com.allobank.allo_backend_test.finance.repository;

import com.allobank.allo_backend_test.finance.exception.ServiceUnavailableException;
import com.allobank.allo_backend_test.finance.model.FinanceResource;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class FinanceRepositoryImpl implements FinanceRepository {
    private final Map<String, FinanceResource> mutableData = new HashMap<>();
    private final AtomicReference<Map<String, FinanceResource>> immutableData = new AtomicReference<>();
    private boolean locked = false;

    @Override
    public void put(String resourceType, FinanceResource resource) {
        mutableData.put(resourceType, resource);
    }

    @Override
    public FinanceResource get(String resourceType) {
        if (!locked) {
            throw new ServiceUnavailableException("Service is not yet initialized");
        }
        return immutableData.get().get(resourceType);
    }

    @Override
    public Map<String, FinanceResource> getData() {
        if (!locked) {
            throw new ServiceUnavailableException("Service is not yet initialized");
        }
        return immutableData.get();
    }

    @Override
    public void lock() {
        immutableData.set(Collections.unmodifiableMap(new HashMap<>(mutableData)));
        locked = true; // cegah race condition
    }
}