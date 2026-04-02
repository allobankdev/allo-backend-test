package com.allobank.finance.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class InMemoryFinanceDataRepository implements FinanceDataRepository {

    private final ConcurrentMap<String, List<Map<String, Object>>> loadingStore = new ConcurrentHashMap<>();
    private final AtomicReference<Map<String, List<Map<String, Object>>>> publishedStore = new AtomicReference<>(Map.of());
    private final AtomicBoolean sealed = new AtomicBoolean(false);

    @Override
    public void saveData(String resourceType, List<Map<String, Object>> data) {
        if (resourceType == null || data == null) {
            return;
        }
        if (sealed.get()) {
            throw new IllegalStateException("Repository is already sealed");
        }
        loadingStore.put(resourceType, toImmutableList(data));
    }

    @Override
    public Optional<List<Map<String, Object>>> findDataByResourceType(String resourceType) {
        Map<String, List<Map<String, Object>>> currentStore = sealed.get() ? publishedStore.get() : loadingStore;
        return Optional.ofNullable(currentStore.get(resourceType));
    }

    @Override
    public void seal() {
        if (!sealed.compareAndSet(false, true)) {
            return;
        }
        publishedStore.set(Map.copyOf(loadingStore));
        loadingStore.clear();
    }

    private List<Map<String, Object>> toImmutableList(List<Map<String, Object>> source) {
        List<Map<String, Object>> items = new ArrayList<>(source.size());
        for (Map<String, Object> item : source) {
            items.add(toImmutableMap(item));
        }
        return List.copyOf(items);
    }

    private Map<String, Object> toImmutableMap(Map<String, Object> source) {
        Map<String, Object> target = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            target.put(entry.getKey(), toImmutableValue(entry.getValue()));
        }
        return Map.copyOf(target);
    }

    @SuppressWarnings("unchecked")
    private Object toImmutableValue(Object value) {
        if (value instanceof Map<?, ?> mapValue) {
            Map<String, Object> target = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : mapValue.entrySet()) {
                target.put(String.valueOf(entry.getKey()), toImmutableValue(entry.getValue()));
            }
            return Map.copyOf(target);
        }
        if (value instanceof List<?> listValue) {
            List<Object> target = new ArrayList<>(listValue.size());
            for (Object item : listValue) {
                target.add(toImmutableValue(item));
            }
            return List.copyOf(target);
        }
        return value;
    }
}
