package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.service.DataStoreService;
import com.allobank.allobackendtest.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/finance/data")
@RequiredArgsConstructor
@Slf4j
public class RateController {
    private final Map<String, IDRDataFetcher> strategies;
    private final DataStoreService dataStoreService;

    @GetMapping("/{resourceType}")
    public ResponseEntity<Object> getRate(@PathVariable String resourceType){
        log.info("Received request for resource type: {}", resourceType);
        Optional.ofNullable(strategies.get(resourceType))
                .orElseThrow(() -> new IllegalArgumentException("Invalid resourceType: " + resourceType));

        return ResponseEntity.ok(dataStoreService.getData(resourceType));

    }
}
