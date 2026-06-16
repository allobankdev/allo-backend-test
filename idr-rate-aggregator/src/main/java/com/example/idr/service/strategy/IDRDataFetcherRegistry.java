package com.example.idr.service.strategy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class IDRDataFetcherRegistry {

    private final Map<String, IDRDataFetcher> fetcherMap;

    public IDRDataFetcherRegistry(List<IDRDataFetcher> fetchers) {
        this.fetcherMap = fetchers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        IDRDataFetcher::getResourceType,
                        Function.identity()
                ));
    }

    public IDRDataFetcher getFetcher(String resourceType) {
        IDRDataFetcher fetcher = fetcherMap.get(resourceType);
        if (fetcher == null) {
            throw new IllegalArgumentException(
                    "Unsupported resource type: " + resourceType
            );
        }
        return fetcher;
    }

    public Map<String, IDRDataFetcher> getAll() {
        return fetcherMap;
    }
}
