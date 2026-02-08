package com.allobank.test.service;

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

    public FinanceService(List<IdrDataFetcher> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(IdrDataFetcher::getResourceType, Function.identity()));
    }

    /**
     * Method ini akan digunakan oleh Runner saat startup untuk mengambil semua data.
     */
    public CompletableFuture<?> fetchDataForResource(String resourceType) {
        IdrDataFetcher strategy = strategyMap.get(resourceType);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid resource type: " + resourceType);
        }
        return strategy.fetchData();
    }

}
