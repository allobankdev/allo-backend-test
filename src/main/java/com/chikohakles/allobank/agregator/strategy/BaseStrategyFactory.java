package com.chikohakles.allobank.agregator.strategy;

import com.chikohakles.allobank.agregator.constant.ResourceType;
import com.chikohakles.allobank.agregator.service.AgregatorService;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseStrategyFactory {
    private final Map<ResourceType, BaseStrategy> strategyMap = new EnumMap<>(ResourceType.class);

    public BaseStrategyFactory(List<BaseStrategy> baseStrategies) {
        for (BaseStrategy baseStrategy : baseStrategies) {
            strategyMap.put(baseStrategy.getResourceType(), baseStrategy);
        }
    }

    public BaseStrategy getStrategy(ResourceType resourceType) {
        BaseStrategy baseStrategy = strategyMap.get(resourceType);
        if (baseStrategy == null) {
            throw new IllegalArgumentException("No strategy found for resource type " + resourceType);
        }
        return baseStrategy;
    }
}
