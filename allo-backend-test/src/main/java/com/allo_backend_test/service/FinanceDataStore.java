package com.allo_backend_test.service;

import com.allo_backend_test.dto.FinanceResponseDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class FinanceDataStore {

    private Map<String, List<FinanceResponseDto>> data = Map.of();

    public synchronized void initialize(Map<String, List<FinanceResponseDto>> initialData) {
        // deep unmodifiable
        this.data = Collections.unmodifiableMap(
                initialData.entrySet().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                Map.Entry::getKey,
                                e -> List.copyOf(e.getValue())
                        ))
        );
    }

    public List<FinanceResponseDto> getByResourceType(String resourceType) {
        return data.get(resourceType);
    }

    public boolean contains(String resourceType) {
        return data.containsKey(resourceType);
    }

}
