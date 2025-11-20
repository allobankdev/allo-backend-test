package com.athallah.finance.cache;

import com.athallah.finance.util.constant.ResourceType;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FinanceDataStore {

    private final Map<ResourceType, Object> store = new ConcurrentHashMap<>();

    public void put(ResourceType type, Object data) {
        store.put(type, data);
    }

    public Object get(ResourceType type) {
        return store.get(type);
    }

    public Map<ResourceType, Object> getAllImmutable() {
        return Collections.unmodifiableMap(store);
    }
}