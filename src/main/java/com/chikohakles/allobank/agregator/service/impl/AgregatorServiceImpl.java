package com.chikohakles.allobank.agregator.service.impl;

import com.chikohakles.allobank.agregator.constant.ResourceType;
import com.chikohakles.allobank.agregator.service.AgregatorService;
import com.chikohakles.allobank.agregator.strategy.BaseStrategy;
import com.chikohakles.allobank.agregator.strategy.BaseStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgregatorServiceImpl implements AgregatorService {
    private final BaseStrategyFactory baseStrategyFactory;
    @Override
    public Object getData(String resourceType) {
        ResourceType resource = ResourceType.valueOf(resourceType.toUpperCase());
        BaseStrategy baseStrategy = baseStrategyFactory.getStrategy(resource);
        return baseStrategy.getData();
    }
}
