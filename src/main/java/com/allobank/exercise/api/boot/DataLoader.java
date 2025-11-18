package com.allobank.exercise.api.boot;

import com.allobank.exercise.api.cache.ResourceCache;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataLoader implements ApplicationRunner {

    private final ResourceCache resourceCache;

    public DataLoader(ResourceCache resourceCache) {
        this.resourceCache = resourceCache;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Object> clientData = new HashMap<>();
        resourceCache.initImmutableCache(clientData);
    }
}
