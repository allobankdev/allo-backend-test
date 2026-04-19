package com.allobankdev.exchangrate.service.store;

import com.allobankdev.exchangrate.execption.NotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataStore {
    private final Map<String, Object> store = new ConcurrentHashMap<>();

    public void save(String key, Object value) {
        store.put(key, value);
    }

    public Object get(String key) {
        return Optional.ofNullable(store.get(key)).orElseThrow(
                () -> new NotFoundException("Resource type"));
    }
}
