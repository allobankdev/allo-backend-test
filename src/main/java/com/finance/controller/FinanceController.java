package com.finance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finance.dto.ApiResponse;
import com.finance.factory.FinanceStrategyFactory;
import com.finance.strategy.FinanceStrategy;


@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceStrategyFactory strategyFactory;

    public FinanceController(FinanceStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    @GetMapping
    public Object getFinanceData(@RequestParam String type) {
        
        FinanceStrategy strategy = strategyFactory.getStrategy(type);
        
        Object result = strategy.execute();

        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Success get " + type + " data",
                result
            )
        );
    }
    
}
