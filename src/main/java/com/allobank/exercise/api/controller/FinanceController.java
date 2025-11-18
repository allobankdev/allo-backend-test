package com.allobank.exercise.api.controller;

import com.allobank.exercise.api.enumeration.ResourceType;
import com.allobank.exercise.api.service.IDRDataFetcher;
import com.allobank.exercise.api.service.impl.IDRDataFetcherStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final IDRDataFetcherStrategy idrDataFetcherStrategy;

    public FinanceController(IDRDataFetcherStrategy idrDataFetcherStrategy) {
        this.idrDataFetcherStrategy = idrDataFetcherStrategy;
    }

    @GetMapping("/data/{resourceType}")
    public ResponseEntity getData(@PathVariable("resourceType") ResourceType resourceType){
        return ResponseEntity.ok(idrDataFetcherStrategy.fetch(resourceType));
    }
}
