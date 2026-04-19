package com.allobankdev.exchangrate.service.store;

import com.allobankdev.exchangrate.constant.ResourceType;
import com.allobankdev.exchangrate.execption.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataStore {
    private final Map<ResourceType, Object> store = new ConcurrentHashMap<>();

    public void save(ResourceType key, Object value) {
        store.put(key, value);
    }

    public Object get(String key) {
        ResourceType resourceType = Optional.ofNullable(ResourceType.getFromName(key)).orElseThrow(
                () -> new NotFoundException("Resource type")
        );
        return store.get(resourceType);
    }
}
