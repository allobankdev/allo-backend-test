package com.chikohakles.allobank.agregator.store;

import com.chikohakles.allobank.agregator.constant.ResourceType;
import com.chikohakles.allobank.agregator.strategy.BaseStrategy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

@Component
public class AgregatorDataStore {
    private final EnumMap<ResourceType, Object> data = new EnumMap<>(ResourceType.class);

    public synchronized void put(ResourceType resource, Object result) {
        data.put(resource, result);
    }

    public synchronized Object get(ResourceType resource) {
        Object result = data.get(resource);
        if (result == null) {
            throw new IllegalArgumentException("Data doesn't exists");
        };
        return result;
    }

    public synchronized void remove(ResourceType resource) {
        data.remove(resource);
    }

    public synchronized Map<ResourceType, Object> getSnapshot() {
        return Collections.unmodifiableMap(new EnumMap<>(data));
    }
}
