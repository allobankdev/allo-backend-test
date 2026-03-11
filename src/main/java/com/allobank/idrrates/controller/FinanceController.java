package com.allobank.idrrates.controller;

import com.allobank.idrrates.dto.ApiResponse;
import com.allobank.idrrates.service.DataStoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final DataStoreService dataStoreService;

    public FinanceController(DataStoreService dataStoreService) {
        this.dataStoreService = dataStoreService;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<ApiResponse<?>> getData(@PathVariable String resourceType) {
        List<?> data = dataStoreService.getData(resourceType);
        return ResponseEntity.ok(ApiResponse.of(resourceType, data));
    }
}
