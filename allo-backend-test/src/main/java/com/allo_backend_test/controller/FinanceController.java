package com.allo_backend_test.controller;

import com.allo_backend_test.dto.FinanceResponseDto;
import com.allo_backend_test.service.FinanceDataStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceDataStore dataStore;

    public FinanceController(FinanceDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<List<FinanceResponseDto>> getFinanceData(
            @PathVariable String resourceType
    ) {
        if (!dataStore.contains(resourceType)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                dataStore.getByResourceType(resourceType)
        );
    }

}
