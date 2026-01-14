package com.allobank.finance.runner;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.allobank.finance.strategy.IDRDataFetcher;

@Component
public class FinanceDataLoader {

    private final Map<String, IDRDataFetcher> strategyMap;

    public FinanceDataLoader(Map<String, IDRDataFetcher> fetchers) {
        this.strategyMap = fetchers.values()
                .stream()
                .collect(Collectors.toMap(
                        IDRDataFetcher::getResourceType,
                        f -> f));
    }
}
