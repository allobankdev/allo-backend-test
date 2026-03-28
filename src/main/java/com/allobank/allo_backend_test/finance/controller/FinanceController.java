package com.allobank.allo_backend_test.finance.controller;

import com.allobank.allo_backend_test.finance.model.FinanceResource;
import com.allobank.allo_backend_test.finance.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/{resourceType}")
    public FinanceResource get(@PathVariable String resourceType) {
        return financeService.findByResourceType(resourceType);
    }
}