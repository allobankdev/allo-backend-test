package com.allo.test.service.impl;

import com.allo.test.service.FinanceService;
import com.allo.test.strategy.FinanceStrategyFactory;
import com.allo.test.strategy.IDRDataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    private FinanceStrategyFactory financeStrategyFactory;

    @Override
    public Object fetchFrankfurtData(String resourceType) {
        IDRDataFetcher fetcher = financeStrategyFactory.getStrategy(resourceType);
        return fetcher.fetchData();
    }
}
