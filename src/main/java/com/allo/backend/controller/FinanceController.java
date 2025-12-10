package com.allo.backend.controller;

import com.allo.backend.dto.ApiResponse;
import com.allo.backend.service.FinanceDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceDataService financeDataService;

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<ApiResponse<Object>> getFinanceData(@PathVariable String resourceType) {
        log.info("Received request for resource type: {}", resourceType);

        Object data = financeDataService.getFinanceData(resourceType);

        log.info("Successfully retrieved data for resource type: {}", resourceType);
        return ResponseEntity.ok(ApiResponse.success(
                "Data retrieved successfully for resource type: " + resourceType,
                data
        ));
    }
}
