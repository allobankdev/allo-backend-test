package com.allobank.backend.test.factory;

import com.allobank.backend.test.strategy.DataStrategy;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class StrategyFactory {

    private final Map<String, DataStrategy> strategies;

    public StrategyFactory(Map<String, DataStrategy> strategies) {
        this.strategies = strategies;
    }

    public DataStrategy getStrategy(String resourceType) {
        DataStrategy strategy = strategies.get(resourceType);

        if (strategy == null) {
            throw new RuntimeException("Invalid resourceType: " + resourceType);
        }
        return strategy;
    }
}