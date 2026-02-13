package com.mlutfiazizan13.allo_backend_test.controller;

import com.mlutfiazizan13.allo_backend_test.dto.ApiResponse;
import com.mlutfiazizan13.allo_backend_test.service.IDRDataStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final IDRDataStore dataStore;

    public FinanceController(IDRDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<ApiResponse<Object>> getData(@PathVariable String resourceType) {
        Object data = dataStore.getData(resourceType);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }
}
