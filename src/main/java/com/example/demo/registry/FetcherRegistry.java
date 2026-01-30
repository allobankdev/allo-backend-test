package com.example.demo.registry;

import com.example.demo.strategy.IDRDataFetcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FetcherRegistry {

    private final Map<String, IDRDataFetcher> fetcherMap;

    public FetcherRegistry(List<IDRDataFetcher> fetchers) {
        this.fetcherMap = fetchers.stream()
                .collect(Collectors.toMap(IDRDataFetcher::getType, f -> f));
    }

    public IDRDataFetcher getFetcher(String type) {
        return fetcherMap.get(type);
    }
}
