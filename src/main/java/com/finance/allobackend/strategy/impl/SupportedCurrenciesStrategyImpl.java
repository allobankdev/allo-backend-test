package com.finance.allobackend.strategy.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.finance.allobackend.strategy.FinanceStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicReference;


@Service
public class SupportedCurrenciesStrategyImpl implements FinanceStrategy {
    private final AtomicReference<Object> cachedData = new AtomicReference<>();

    @Override
    public String getResourceType() {
        return "supportedCurrencies";
    }

    @Override
    public Object getCacheData() {
        return cachedData.get();
    }

    @Override
    public void getOrRefreshData(RestTemplate restTemplate) {
        JsonNode response = restTemplate.getForObject("/currencies", JsonNode.class);
        cachedData.set(response);
    }
}
