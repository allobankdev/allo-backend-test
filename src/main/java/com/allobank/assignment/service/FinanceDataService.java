package com.allobank.assignment.service;

import com.allobank.assignment.model.FinanceDataResponse;
import com.allobank.assignment.strategy.IdrDataStrategyRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceDataService {

    private final FinanceDataCache cache;
    private final IdrDataStrategyRegistry strategyRegistry;

    public FinanceDataService(FinanceDataCache cache, IdrDataStrategyRegistry strategyRegistry) {
        this.cache = cache;
        this.strategyRegistry = strategyRegistry;
    }


    public List<FinanceDataResponse> getFinanceData(String resourceType) {
        return null;
    }
}
