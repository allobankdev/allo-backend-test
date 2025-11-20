package com.allobank.assignment.strategy;

import com.allobank.assignment.model.ResourceType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class IdrDataStrategyRegistry {

    private final Map<ResourceType, IdrDataFetchStrategy> strategies;

    public IdrDataStrategyRegistry(List<IdrDataFetchStrategy> strategies) {
        EnumMap<ResourceType, IdrDataFetchStrategy> map = new EnumMap<>(ResourceType.class);
        for (IdrDataFetchStrategy strategy : strategies) {
            map.put(strategy.supports(), strategy);
        }
        this.strategies = Map.copyOf(map);

    }

    public IdrDataFetchStrategy getStrategy(ResourceType resourceType) {
        return strategies.get(resourceType);
    }

    public Collection<IdrDataFetchStrategy> getAllStrategies() {
        return strategies.values();
    }
}
