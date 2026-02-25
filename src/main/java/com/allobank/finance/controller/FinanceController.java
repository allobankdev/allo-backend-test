package com.allobank.finance.controller;

import com.allobank.finance.dto.FinanceDataResponse;
import com.allobank.finance.service.FinanceDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceDataService financeDataService;

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<FinanceDataResponse> getFinanceData(@PathVariable String resourceType) {

        log.info("GET /api/v1/finance/data/{}", resourceType);
        FinanceDataResponse response = financeDataService.getByResourceType(resourceType);
        return ResponseEntity.ok((response));
    }
}
