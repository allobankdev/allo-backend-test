package com.example.technical_test_allo_bank.controller;

import com.example.technical_test_allo_bank.service.FinanceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping("/{type}")
    public Object getData(@PathVariable String type) {
        return financeService.getData(type);
    }
}
