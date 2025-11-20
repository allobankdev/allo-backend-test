package com.allobank.assignment.service;

import com.allobank.assignment.model.FinanceDataResponse;
import com.allobank.assignment.model.ResourceType;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class FinanceDataCache {

    private final AtomicReference<Map<ResourceType, FinanceDataResponse>> cache = new AtomicReference<>(Collections.emptyMap());

    public void initialize(Map<ResourceType, FinanceDataResponse> value) {
        Map<ResourceType, FinanceDataResponse> immutable = Map.copyOf(value);
        boolean updated = cache.compareAndSet(Collections.emptyMap(), immutable);
        if (!updated) {
            throw new IllegalStateException("Finance data cache has already been initialized");
        }
    }

    public FinanceDataResponse get(ResourceType resourceType) {
        FinanceDataResponse response = cache.get().get(resourceType);
        if (response == null) {
            throw new IllegalStateException("Finance data cache has not been initialized");
        }
        return response;
    }
}
