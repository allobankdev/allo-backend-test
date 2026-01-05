package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.payload.ResourceTypeNotFoundException;
import com.bezkoder.springjwt.service.StrategyRegistry;
import com.bezkoder.springjwt.strategy.IDRDataFetcherStrategy;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final Map<String, IDRDataFetcherStrategy> strategyMap;

    public FinanceController(StrategyRegistry registry) {
        this.strategyMap = registry.strategyMap();
    }

    @GetMapping("/data/{resourceType}")
    public List<Object> getData(@PathVariable String resourceType) {
        return Optional.ofNullable(strategyMap.get(resourceType))
                .map(IDRDataFetcherStrategy::getData)
                .orElseThrow(() -> new ResourceTypeNotFoundException(resourceType));
    }
}
