package com.allobank.allobackendtest.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.strategy.IDRDataFetcher;

@Service
public class IDRDataFetcherRegistry {

    private final Map<String, IDRDataFetcher> fetcherMap;

    public IDRDataFetcherRegistry(List<IDRDataFetcher> fetchers) {
        this.fetcherMap = fetchers.stream().collect(Collectors.toUnmodifiableMap(
                        IDRDataFetcher::getResourceType,
                        fetcher -> fetcher));
    }

    public IDRDataFetcher getFetcher(String resourceType) {
        IDRDataFetcher fetcher = fetcherMap.get(resourceType);
        if (fetcher == null) {
            throw new IllegalArgumentException(
                    "Unsupported resourceType: " + resourceType);
        }
        return fetcher;
    }

    public Map<String, IDRDataFetcher> getAllFetchers() {
        return fetcherMap;
    }

}
