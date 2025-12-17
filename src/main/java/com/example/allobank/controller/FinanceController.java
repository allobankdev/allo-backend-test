package com.example.allobank.controller;

import com.example.allobank.dto.FinanceDataItemDto;
import com.example.allobank.exception.ResourceNotFoundException;
import com.example.allobank.service.IDRDataFetcher;
import com.example.allobank.storage.DataStorageService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    /**
     * Key = bean name (we set bean name equal to resourceType).
     * This satisfies "map-based lookup injected by Spring" and avoids if/else/switch.
     */
    private final Map<String, IDRDataFetcher> fetchersByResourceType;

    private final DataStorageService storage;

    @GetMapping("/data/{resourceType}")
    public List<FinanceDataItemDto> getFinanceData(@PathVariable String resourceType) {
        IDRDataFetcher fetcher = fetchersByResourceType.get(resourceType);
        if (fetcher == null) {
            throw new ResourceNotFoundException("Unknown resourceType: " + resourceType);
        }

        return storage.getByResourceType(fetcher.resourceType());
    }
}