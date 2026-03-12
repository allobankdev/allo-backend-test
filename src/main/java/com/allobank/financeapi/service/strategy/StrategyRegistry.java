package com.allobank.financeapi.service.strategy;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StrategyRegistry {

    private final List<IDRDataFetcher> strategies;
    private Map<String, IDRDataFetcher> strategyMap;

    @PostConstruct
    public void init() {
        strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        IDRDataFetcher::getResourceType,
                        Function.identity()
                ));
    }

    public IDRDataFetcher getStrategy(String resourceType) {
        IDRDataFetcher strategy = strategyMap.get(resourceType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported resource type: " + resourceType);
        }
        return strategy;
    }
}