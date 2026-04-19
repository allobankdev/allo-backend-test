package com.allobankdev.exchangrate.service.factory;

import com.allobankdev.exchangrate.constant.ResourceType;
import com.allobankdev.exchangrate.service.strategy.IdrDataFetcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DataFetcherFactory {
    private final Map<ResourceType, IdrDataFetcher> strategies;

    public DataFetcherFactory(List<IdrDataFetcher> fetcherList) {
        this.strategies = fetcherList.stream()
                .collect(Collectors.toMap(IdrDataFetcher::getType, f -> f));
    }

    public IdrDataFetcher get(ResourceType type) {
        return Optional.ofNullable(strategies.get(type))
                .orElseThrow(() -> new RuntimeException("Invalid type: " + type.getName()));
    }

    public Set<ResourceType> getAllTypes() {
        return strategies.keySet();
    }

}
