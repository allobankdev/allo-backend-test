package com.allobank.allobackendtest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobackendtest.model.DTO.ApiResponse;
import com.allobank.allobackendtest.store.InMemoryDataStore;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/finance/data")
@Slf4j
public class FinanceDataController {

    private final InMemoryDataStore dataStore;

    public FinanceDataController(InMemoryDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/{resourceType}")
    public ApiResponse<Object> getFinanceData(@PathVariable String resourceType) {
        log.info("Request received for resourceType={}", resourceType);
        
        Object data = dataStore.get(resourceType);

        if (data == null) {
            log.warn("Unsupported or unavailable resourceType={}", resourceType);
            throw new IllegalArgumentException("Unsupported resourceType or data not available: " + resourceType);
        }

        return ApiResponse.success("Data fetched successfully", data);
    }

}
