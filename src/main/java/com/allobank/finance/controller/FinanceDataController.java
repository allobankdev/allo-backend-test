package com.allobank.finance.controller;

import com.allobank.finance.model.response.FinanceDataResponse;
import com.allobank.finance.service.FinanceDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceDataController {

    private final FinanceDataService service;

    @GetMapping("/{resourceType}")
    public FinanceDataResponse get(@PathVariable String resourceType) {
        return service.get(resourceType);
    }
}
