package com.mlutfiazizan13.allo_backend_test.service;

import com.mlutfiazizan13.allo_backend_test.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class IDRDataStore {

    private volatile Map<String, Object> dataStore = Collections.emptyMap();

    public void loadData(Map<String, Object> data) {
        this.dataStore = Collections.unmodifiableMap(new HashMap<>(data));
    }

    public Object getData(String resourceType) {
        Object data = dataStore.get(resourceType);
        if (data == null) {
            throw new ResourceNotFoundException(
                    "Unknown resource type: '" + resourceType
                    + "'. Available: " + dataStore.keySet());
        }
        return data;
    }

    public Set<String> getAvailableResources() {
        return dataStore.keySet();
    }
}
