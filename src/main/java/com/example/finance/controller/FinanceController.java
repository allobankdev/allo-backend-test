package com.example.finance.controller;

import com.example.finance.service.FinanceDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceDataService dataService;

    public FinanceController(FinanceDataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<List<Map<String, Object>>> getData(@PathVariable String resourceType) {
        List<Map<String, Object>> data = dataService.getData(resourceType);
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }
}