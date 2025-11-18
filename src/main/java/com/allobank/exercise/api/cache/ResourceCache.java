package com.allobank.exercise.api.cache;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class ResourceCache {

    private Map<String, Object> dataCache = new HashMap<>();

    public void initImmutableCache(Map<String, Object> dataCache){
        this.dataCache = Collections.unmodifiableMap(dataCache);
    }

    public Map<String, Object> getDataCache(){
        return this.dataCache;
    }
}
