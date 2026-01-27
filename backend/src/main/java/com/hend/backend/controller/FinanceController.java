package com.hend.backend.controller;

import com.hend.backend.exception.ResourceNotFoundException;
import com.hend.backend.service.FinanceDataStorage;
import com.hend.backend.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author : hend wunga
 */

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceDataStorage storage;
    // Spring akan otomatis menyuntikkan semua bean IDRDataFetcher ke Map ini
    private final Map<String, IDRDataFetcher> strategyMap;

    @GetMapping("/{resourceType}")
    public Object getData(@PathVariable String resourceType) {
        // Validasi apakah resourceType ada di daftar strategi kita (Memenuhi Constraint A)
        if (!strategyMap.containsKey(resourceType)) {
            throw new ResourceNotFoundException("Resource type '" + resourceType + "' is not supported.");
        }

        Object data = storage.getData(resourceType);
        if (data == null) {
            throw new ResourceNotFoundException("Data for '" + resourceType + "' is currently unavailable.");
        }

        return data;
    }
}
