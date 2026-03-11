package com.allo.idraggregator.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allo.idraggregator.application.service.FinanceDataService;
import com.allo.idraggregator.presentation.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/finance/data")
public class FinanceDataController {

    private final FinanceDataService service;
    
    @GetMapping("/{resourceType}")
    public ResponseEntity<ApiResponse<Object>> getData(
            @PathVariable String resourceType) {

        return ResponseEntity.ok(
            ApiResponse.response(200, "Success", service.get(resourceType))
        );
    }
}
