package com.allo.test.store;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FrankfurterCacheService {

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        cache.put(key, value);
    }

    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(cache.get(key));
    }

    public boolean contains(String key) {
        return cache.containsKey(key);
    }

}
