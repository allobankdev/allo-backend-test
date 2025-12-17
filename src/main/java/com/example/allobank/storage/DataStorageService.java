package com.example.allobank.storage;

import com.example.allobank.dto.FinanceDataItemDto;
import com.example.allobank.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.stereotype.Service;

@Service
public class DataStorageService {

    /**
     * Immutable snapshot after startup.
     * AtomicReference ensures safe publication across threads.
     */
    private final AtomicReference<Map<String, List<FinanceDataItemDto>>> snapshot =
            new AtomicReference<>();

    /**
     * Called once by DataLoaderRunner after all data fetched.
     */
    public void initialize(Map<String, List<FinanceDataItemDto>> data) {
        if (data == null) data = Map.of();

        Map<String, List<FinanceDataItemDto>> immutable = data.entrySet().stream()
                .collect(java.util.stream.Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        e -> List.copyOf(e.getValue())
                ));

        boolean set = snapshot.compareAndSet(null, immutable);
        if (!set) {
            throw new IllegalStateException("DataStorageService already initialized");
        }
    }

    public List<FinanceDataItemDto> getByResourceType(String resourceType) {
        Map<String, List<FinanceDataItemDto>> data = snapshot.get();
        if (data == null) {
            throw new IllegalStateException("Data is not initialized yet");
        }

        List<FinanceDataItemDto> result = data.get(resourceType);
        if (result == null) {
            throw new ResourceNotFoundException("Unknown resourceType: " + resourceType);
        }
        return result;
    }
}