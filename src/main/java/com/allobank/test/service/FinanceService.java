package com.allobank.test.service;

import com.allobank.test.repository.FinanceDataRepository;
import com.allobank.test.strategy.IdrDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FinanceService {

    private final Map<String, IdrDataFetcher> strategyMap;
    private final FinanceDataRepository repository;

    public FinanceService(List<IdrDataFetcher> strategies, FinanceDataRepository repository) {
        this.repository = repository;
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(IdrDataFetcher::getResourceType, Function.identity()));
    }

    public CompletableFuture<Void> fetchAndCacheAllData() {
        List<CompletableFuture<Void>> futures = strategyMap.values().stream()
                .map(strategy -> strategy.fetchData().thenAccept(data -> {
                    String key = strategy.getResourceType();
                    log.info("Caching data for resource: {}", key);
                    repository.saveData(key, data);
                }))
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    public Object getCachedData(String resourceType) {
        if (!strategyMap.containsKey(resourceType)) {
            throw new IllegalArgumentException("Invalid resource type: " + resourceType);
        }

        Object data = repository.getData(resourceType);
        if (data == null) {
            throw new IllegalStateException("Data not initialized. Server might be starting up.");
        }
        return data;
    }
}