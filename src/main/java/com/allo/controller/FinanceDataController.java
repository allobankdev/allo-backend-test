package com.allo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allo.dto.FinanceResourceResponse;
import com.allo.service.FinanceDataService;

@RestController
@RequestMapping("/api/finance")
public class FinanceDataController {

    private final FinanceDataService financeDataService;

    public FinanceDataController(FinanceDataService financeDataService) {
        this.financeDataService = financeDataService;
    }

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<List<FinanceResourceResponse>> getData(
            @PathVariable String resourceType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        List<FinanceResourceResponse> data = financeDataService.getDataByResourceType(
                resourceType, startDate, endDate);
        return ResponseEntity.ok(data);
    }
}
