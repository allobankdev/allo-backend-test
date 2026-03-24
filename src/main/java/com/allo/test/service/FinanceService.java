package com.allo.test.service;

import com.allo.test.strategy.IDRDataFetcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FinanceService {

    private final Map<String , IDRDataFetcher> strategyMap;
    public FinanceService(List<IDRDataFetcher> fetchers) {
        this.strategyMap = fetchers.stream()
                .collect(Collectors.toMap(
                        IDRDataFetcher::getResourceType,
                        f -> f
                ));
    }

    public List<Object> getData(String resourceType) {
        IDRDataFetcher fetcher = strategyMap.get(resourceType);

        if (fetcher == null) {
            throw new RuntimeException("Invalid resource type");
        }

        return fetcher.fetchData();
    }
}
