package com.allobank.backend.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.backend.store.FinanceDataStore;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceDataStore dataStore;

    @GetMapping(value = "/data/{resourceType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFinanceData(@PathVariable String resourceType) {
        
        JsonNode data = dataStore.getData(resourceType);
        
        String jsonString = data.toPrettyString();
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonString);
    }
}