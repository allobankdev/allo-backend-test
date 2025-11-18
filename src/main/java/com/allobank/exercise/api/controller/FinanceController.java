package com.allobank.exercise.api.controller;

import com.allobank.exercise.api.enumeration.ResourceType;
import com.allobank.exercise.api.service.IDRDataFetcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final Map<String, IDRDataFetcher> serviceSelector;

    public FinanceController(Map<String, IDRDataFetcher> serviceSelector) {
        this.serviceSelector = serviceSelector;
    }

    @GetMapping("/data/{resourceType}")
    public ResponseEntity getData(@PathVariable("resourceType") ResourceType resourceType){
        IDRDataFetcher idrDataFetcher = serviceSelector.get(resourceType.getPath());
        return ResponseEntity.ok(idrDataFetcher.getData());
    }
}
