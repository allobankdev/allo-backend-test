package com.allobank.finance.controller;

import com.allobank.finance.model.ApiResponse;
import com.allobank.finance.model.FinanceDataResult;
import com.allobank.finance.service.FinanceDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final FinanceDataService financeDataService;

    public FinanceController(FinanceDataService financeDataService) {
        this.financeDataService = financeDataService;
    }

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<ApiResponse<List<FinanceDataResult>>> getFinanceData(
            @PathVariable String resourceType) {

        log.info("Request diterima: GET /api/finance/data/{}", resourceType);

        List<FinanceDataResult> data = financeDataService.getData(resourceType);

        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
