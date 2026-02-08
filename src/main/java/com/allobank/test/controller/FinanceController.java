package com.allobank.test.controller;

import com.allobank.test.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<List<Object>> getFinanceData(@PathVariable String resourceType) {
        Object data = financeService.getCachedData(resourceType);

        return ResponseEntity.ok(Collections.singletonList(data));
    }
}