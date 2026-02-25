package com.allobank.finance.controller;

import com.allobank.finance.cache.IDRDataFetcherCache;
import com.allobank.finance.model.FinanceData;
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
public class FinanceController {

    private final IDRDataFetcherCache idrDataFetcherCache;

    @GetMapping("/{resourceType}")
    public ResponseEntity<FinanceData> getData(@PathVariable String resourceType) {
        log.debug("GET /api/finance/data/{}", resourceType);
        FinanceData financeData = idrDataFetcherCache.get(resourceType);
        return ResponseEntity.ok(financeData);
    }
}
