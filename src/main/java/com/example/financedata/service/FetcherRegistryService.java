package com.example.financedata.service;

import com.example.financedata.fetcher.IDRDataFetcher;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FetcherRegistryService {

    private final Map<String, IDRDataFetcher> fetcherMap;

    // Map injection: Spring auto wires all beans implementing IDRDataFetcher as a map with bean name key.
    public FetcherRegistryService(Map<String, IDRDataFetcher> fetcherMap) {
        this.fetcherMap = fetcherMap;
    }

    public IDRDataFetcher getByResourceKey(String resourceKey) {
        return fetcherMap.values().stream()
                .filter(f -> f.resourceKey().equals(resourceKey))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown resourceType: " + resourceKey));
    }

    public Map<String, IDRDataFetcher> all() {
        return fetcherMap;
    }
}
