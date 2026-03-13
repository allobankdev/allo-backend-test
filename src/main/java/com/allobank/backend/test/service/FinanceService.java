package com.allobank.backend.test.service;

import com.allobank.backend.test.factory.StrategyFactory;
import com.allobank.backend.test.model.ApiResult;
import com.allobank.backend.test.strategy.DataStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final StrategyFactory factory;

    public List<ApiResult> getFinanceData(String resourceType) {
        DataStrategy strategy = factory.getStrategy(resourceType);
        return List.of(strategy.execute());
    }
}