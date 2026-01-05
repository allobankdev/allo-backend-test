package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.strategy.IDRDataFetcherStrategy;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class StrategyRegistry {

    private final Map<String, IDRDataFetcherStrategy> strategyMap;

    public StrategyRegistry(List<IDRDataFetcherStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toUnmodifiableMap(IDRDataFetcherStrategy::resourceType, Function.identity()));
    }

    public Map<String, IDRDataFetcherStrategy> strategyMap() {
        return strategyMap;
    }
}
