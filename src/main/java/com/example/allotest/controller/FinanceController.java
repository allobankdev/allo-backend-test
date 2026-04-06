package com.example.allotest.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.allotest.strategy.IDRDataFetcher;

@RestController
@RequestMapping("/finance/data")
public class FinanceController {
    private final Map<String, IDRDataFetcher> strategies;

    public FinanceController(Map<String, IDRDataFetcher> strategies) {
        this.strategies = strategies;
    }

    @GetMapping("/{type}")
    public Object getData(@PathVariable String type) {
        IDRDataFetcher strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid data type: " + type);
        }
        return strategy.getData();
    }
}
