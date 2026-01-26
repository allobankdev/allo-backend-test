package com.example.AlloBank.controller;

import com.example.AlloBank.strategy.FinanceDataStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final Map<String, FinanceDataStrategy<?>> strategyMap;

    public FinanceController(List<FinanceDataStrategy<?>> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(FinanceDataStrategy::getType, s -> s));
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getData(@PathVariable String resourceType) {
        FinanceDataStrategy<?> strategy = strategyMap.get(resourceType);

        if (strategy == null) {
            throw new IllegalArgumentException("Unknown resourceType: " + resourceType);
        }

        return ResponseEntity.ok(strategy.getData());
    }

}
