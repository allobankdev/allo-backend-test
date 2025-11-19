package com.finance.allobackend.controller;

import com.finance.allobackend.strategy.FinanceStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {
    private final Map<String, FinanceStrategy> strategyMap;

    public FinanceController(List<FinanceStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(FinanceStrategy::getResourceType, Function.identity()));
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
        FinanceStrategy strategy = strategyMap.get(resourceType);

        if (strategy == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Resource type is invalid. The supported resource: " + strategyMap.keySet()));
        }

        Object data = strategy.getCacheData();

        if (data == null) {
            return ResponseEntity.status(503)
                    .body(Map.of("error", "Data not initialized or fetch failed."));
        }
        return ResponseEntity.ok(data);
    }
}
