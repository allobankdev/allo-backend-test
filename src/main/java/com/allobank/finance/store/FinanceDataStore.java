package com.allobank.finance.store;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FinanceDataStore {

    private volatile Map<String, List<Map<String, Object>>> data = Map.of();

    public void replaceAll(Map<String, List<Map<String, Object>>> newData) {
        this.data = newData.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> List.copyOf(entry.getValue().stream()
                                .map(this::immutableMap)
                                .toList())));
    }

    public Optional<List<Map<String, Object>>> getData(String resourceType) {
        return Optional.ofNullable(data.get(resourceType));
    }

    public Set<String> supportedResourceTypes() {
        return data.keySet();
    }

    private Map<String, Object> immutableMap(Map<String, Object> source) {
        return source.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> immutableValue(entry.getValue())));
    }

    @SuppressWarnings("unchecked")
    private Object immutableValue(Object value) {
        if (value instanceof Map<?, ?> mapValue) {
            return immutableMap((Map<String, Object>) mapValue);
        }
        if (value instanceof List<?> listValue) {
            return listValue.stream()
                    .map(this::immutableValue)
                    .toList();
        }
        return value;
    }
}
