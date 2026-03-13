package com.allobank.backend.test.controller;

import com.allobank.backend.test.model.ApiResult;
import com.allobank.backend.test.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService service;

    @GetMapping("/{resourceType}")
    public List<ApiResult> getData(@PathVariable String resourceType) {

        return service.getFinanceData(resourceType);
    }
}