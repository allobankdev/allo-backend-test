package com.allobankdev.exchangrate.service.factory;

import com.allobankdev.exchangrate.service.strategy.IdrDataFetcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DataFetcherFactory {
    private final Map<String, IdrDataFetcher> strategies;

    public DataFetcherFactory(List<IdrDataFetcher> fetcherList) {
        this.strategies = fetcherList.stream()
                .collect(Collectors.toMap(IdrDataFetcher::getType, f -> f));
    }

    public IdrDataFetcher get(String type) {
        return Optional.ofNullable(strategies.get(type))
                .orElseThrow(() -> new RuntimeException("Invalid type: " + type));
    }

    public Set<String> getAllTypes() {
        return strategies.keySet();
    }

}
