package com.zultest.allobank_backend_test.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IDRDataFetcherRegistry {

    private final Map<String, IDRDataFetcherInterface> fetcherMap;

    public IDRDataFetcherRegistry(List<IDRDataFetcherInterface> fetchers) {
        this.fetcherMap = fetchers.stream()
                .collect(Collectors.toMap(
                    IDRDataFetcherInterface::resourceType,
                    Function.identity()
                ));
    }

    public IDRDataFetcherInterface getFetcher(String resourceType) {
        IDRDataFetcherInterface fetcher = fetcherMap.get(resourceType);
        if (fetcher == null) {
            throw new IllegalArgumentException("Unsupported resourceType: " + resourceType);
        }
        return fetcher;
    }
}
