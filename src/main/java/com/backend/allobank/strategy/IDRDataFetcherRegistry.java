package com.backend.allobank.strategy;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class IDRDataFetcherRegistry {

    private final List<IDRDataFetcher> fetchers;
    private Map<String, IDRDataFetcher> strategyMap;

    public IDRDataFetcherRegistry(List<IDRDataFetcher> fetchers) {
        this.fetchers = fetchers;
    }

    @PostConstruct
    public void init() {
        this.strategyMap = fetchers.stream()
                .filter(f -> f.getResourceType() != null)
                .collect(Collectors.toMap(
                        IDRDataFetcher::getResourceType,
                        f -> f,
                        (a, b) -> {
                            throw new IllegalStateException(
                                    "Duplicate resourceType detected: " + a.getResourceType()
                            );
                        }
                ));
    }

    public IDRDataFetcher getStrategy(String resourceType) {
        IDRDataFetcher fetcher = strategyMap.get(resourceType);
        if (fetcher == null) {
            throw new IllegalArgumentException("Unsupported resourceType: " + resourceType);
        }
        return fetcher;
    }
}
