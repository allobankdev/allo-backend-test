package com.example.technical_test_allo_bank.service;

import com.example.technical_test_allo_bank.cache.InMemoryCache;
import com.example.technical_test_allo_bank.strategy.FinanceStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FinanceService {

    private final Map<String, FinanceStrategy> strategyMap;
    private final InMemoryCache cache;

    public FinanceService(List<FinanceStrategy> strategies,
                          InMemoryCache cache) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(FinanceStrategy::getType, s -> s));
        this.cache = cache;
    }

    public Object getData(String type) {
        Object cached = cache.get(type);
        if (cached != null) {
            return cached;
        }

        FinanceStrategy strategy = strategyMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }

        Object result = strategy.load();
        cache.put(type, result);
        return result;
    }
}
