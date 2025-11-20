package com.allobank.assignment.controller;

import com.allobank.assignment.model.FinanceDataResponse;
import com.allobank.assignment.service.FinanceDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceDataController {

    private final FinanceDataService financeDataService;

    public FinanceDataController(FinanceDataService financeDataService) {
        this.financeDataService = financeDataService;
    }


    @GetMapping("/{resourceType}")
    public ResponseEntity<List<FinanceDataResponse>> getFinanceData(@PathVariable String resourceType) {
        List<FinanceDataResponse> data = financeDataService.getFinanceData(resourceType);
        return ResponseEntity.ok(data);
    }

}
