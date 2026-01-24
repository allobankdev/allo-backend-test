package com.sdewa.IdrRateAggregator.controllers;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdewa.IdrRateAggregator.services.AppDataStore;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceController {

    private final AppDataStore dataStore;

    @GetMapping("/{resourceType}")
    public ResponseEntity<Object> getData(@PathVariable String resourceType) {
        Object data = dataStore.get(resourceType);
        if (data == null) {
            throw new ResourceNotFoundException("Resource not found: " + resourceType);
        }
        return ResponseEntity.ok(data);
    }
}
