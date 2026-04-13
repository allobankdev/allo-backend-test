package com.allobank.test.strategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class IDRDataFetcherRegistry {

    private final Map<String, IDRDataFetcher> fetcherMap;

    public IDRDataFetcherRegistry(List<IDRDataFetcher> fetchers) {
        this.fetcherMap = fetchers.stream()
                .collect(Collectors.toUnmodifiableMap(IDRDataFetcher::resourceType, Function.identity()));
    }

    public Map<String, IDRDataFetcher> asMap() {
        return fetcherMap;
    }
}
