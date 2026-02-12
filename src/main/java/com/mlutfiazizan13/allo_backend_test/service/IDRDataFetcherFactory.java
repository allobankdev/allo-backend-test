package com.mlutfiazizan13.allo_backend_test.service;

import com.mlutfiazizan13.allo_backend_test.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class IDRDataFetcherFactory {

    private final Map<String, IDRDataFetcher> strategiesMap;

    public IDRDataFetcherFactory(List<IDRDataFetcher> strategies) {
        this.strategiesMap = strategies.stream()
                .collect(Collectors.toMap(
                        IDRDataFetcher::getStrategyType,
                        Function.identity()
                ));
    }

    public IDRDataFetcher getStrategy(String resourceType) {
        IDRDataFetcher fetcher = strategiesMap.get(resourceType);
        if (fetcher == null) {
            throw new ResourceNotFoundException(
                    "Unknown resource type: '" + resourceType
                    + "'. Available: " + strategiesMap.keySet());
        }
        return fetcher;
    }

    public Set<String> getAvailableStrategies() {
        return strategiesMap.keySet();
    }
}
