package com.allobank.finnance.allobankfinance.service.strategy;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FinanceDataStrategyResolver {

    private final Map<String, FinanceDataStrategy> strategyMap;

    public FinanceDataStrategyResolver(
            List<FinanceDataStrategy> strategies
    ) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        FinanceDataStrategy::getResourceType,
                        Function.identity()
                ));
    }

    public FinanceDataStrategy resolve(String resourceType) {
        return Optional.ofNullable(strategyMap.get(resourceType))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unsupported resourceType: " + resourceType
                        )
                );
    }
}
