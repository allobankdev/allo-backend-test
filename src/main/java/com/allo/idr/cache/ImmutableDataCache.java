package com.allo.idr.cache;

import com.allo.idr.enums.ResourceType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class ImmutableDataCache {
    private volatile Map<ResourceType, List<?>> snapshot = Map.of();

    public synchronized void populate(Map<ResourceType, List<?>> data) {
        if (snapshot.isEmpty()){
            Map<ResourceType, List<?>> temCache = new EnumMap<>(ResourceType.class);
            data.forEach((k, v) -> {
                if (k != null) temCache.put(k, v);
            });
            snapshot = Map.copyOf(temCache);
        }
    }

    public List<?> get(ResourceType key) {
        return snapshot.get(key);
    }
    public Map<ResourceType, List<?>> getAll(){
        return snapshot;
    }

    public synchronized void reset() {
        snapshot = Map.of();
    }
}
