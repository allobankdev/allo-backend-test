package com.musfiul.idrrateaggregator.controller;

import com.musfiul.idrrateaggregator.constant.ResourceType;
import com.musfiul.idrrateaggregator.service.data.FinanceDataStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class AggregatorController {
    private final FinanceDataStoreService storeService;

    @GetMapping("/{resourceType}")
    public ResponseEntity<Object> getDataByResourceType(@PathVariable String resourceType) {
        log.info("Getting data for resource type: {}", resourceType);
        ResourceType type = ResourceType.from(resourceType);
        log.info("Resource type: {}", type);
        return ResponseEntity.ok(storeService.get(type));
    }
}
