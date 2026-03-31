package com.allo.backend.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.allo.backend.strategy.IDRDataFetcher;

@Service
public class FetcherFactory {
    private final Map<String, IDRDataFetcher> fetcherMap;

    public FetcherFactory(List<IDRDataFetcher> fetchers) {
        this.fetcherMap = fetchers.stream()
                .collect(Collectors.toMap(IDRDataFetcher::getType, f -> f));
    }

    public IDRDataFetcher get(String type) {
        return fetcherMap.get(type);
    }
}
