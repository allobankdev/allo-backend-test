package com.test.allo_bank_test_exhange_rate.store;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.test.allo_bank_test_exhange_rate.enums.ResourceType;

@Component
public class ImmutableDataStore {
    
    private volatile Map<String,Object> store = Collections.emptyMap();
    private final Object lock = new Object();

    public void loadInitialData(Map<String,Object> data) {
        synchronized (lock) {
            this.store = Collections.unmodifiableMap(new ConcurrentHashMap<>(data));
        }
    }

    public Object get(String key) {
        if(Stream.of(ResourceType.values()).noneMatch(r -> r.toString().equals(key))) {
            throw new IllegalArgumentException("Unsupported resource type: " + key);
        }
        return store.get(key);
    }

    public Map<String,Object> getAll() {
        return store;
    }
}
