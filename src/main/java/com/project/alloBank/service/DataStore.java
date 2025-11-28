package com.project.alloBank.service;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class DataStore {
    private final AtomicReference<Map<String, Object>> storeRef = new AtomicReference<>(Collections.emptyMap());

    public void setAll(Map<String, Object> map) {
        storeRef.set(Collections.unmodifiableMap(map));
    }

    public Object get(String key) {
        return storeRef.get().get(key);
    }

}
