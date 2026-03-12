package com.allobank.finace.controller;

import com.allobank.finace.exception.ResourceNotFoundException;
import com.allobank.finace.service.DataStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final DataStoreService dataStoreService;

    @Autowired
    public FinanceController(DataStoreService dataStoreService) {
        this.dataStoreService = dataStoreService;
    }

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<List<Map<String, Object>>> getData(@PathVariable String resourceType) {
        log.info("Request received for resource type: {}", resourceType);

        if (!dataStoreService.contains(resourceType)) {
            throw new ResourceNotFoundException(resourceType);
        }

        List<Map<String, Object>> data = dataStoreService.get(resourceType);
        return ResponseEntity.ok(data);
    }
}
