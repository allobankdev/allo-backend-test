package com.allobank.allobanktest.controller;

import com.allobank.allobanktest.dto.ApiResponse;
import com.allobank.allobanktest.store.FinanceDataStore;
import com.allobank.allobanktest.strategy.ResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceDataStore store;

    public FinanceController(FinanceDataStore store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getFinanceData(
            @PathVariable String resourceType
    ) {
        log.info("Received request for finance data: {}", resourceType);

        try {
            ResourceType type = ResourceType.from(resourceType);
            Object data = store.get(type.getValue());

            if (data == null) {
                log.warn("No data found for resource: {}", resourceType);
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(
                    new ApiResponse<>(type.getValue(), data)
            );

        } catch (IllegalArgumentException ex) {
            log.warn("Invalid resource type requested: {}", resourceType);
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
