package com.example.allotest.service;

import com.example.allotest.exceptions.CustomGlobalException;
import com.example.allotest.strategy.IDataFetcher;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ApiClientServiceImpl implements IApiClientService {
    private final Map<String, IDataFetcher> strategies;

    public ApiClientServiceImpl(Map<String, IDataFetcher> strategies) {
        this.strategies = strategies;
    }

    @Override
    public Object[] fetchData(String resourceType) {
        try {
            IDataFetcher strategy = strategies.get(resourceType);
            return strategy.fetchData();
        } catch (Exception e) {
            throw new CustomGlobalException("error occured: " + e.getMessage(), resourceType);
        }
    }
}
