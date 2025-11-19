package com.finance.allobackend.strategy.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.finance.allobackend.strategy.FinanceStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class IdrLatestRateStrategyImpl implements FinanceStrategy {
    private final AtomicReference<Object> cachedData = new AtomicReference<>();
    private static final String githubUname = "nikods761";

    @Override
    public String getResourceType() {
        return "latestIDRRate";
    }

    @Override
    public Object getCacheData() {
        return cachedData.get();
    }

    @Override
    public void getOrRefreshData(RestTemplate restTemplate) {
        JsonNode response = restTemplate.getForObject("/latest?base=IDR", JsonNode.class);
        processAndCache(response);
    }

    private void processAndCache(JsonNode response) {
        double spreadFactor = calculateSpreadFactor();
        double rateUsd = response.path("rates").path("USD").asDouble();
        double buySpreadUsdToIdr = (1.0 / rateUsd) * (1.0 + spreadFactor);

        ObjectNode cached = response.deepCopy();
        cached.put("buySpreadUSDtoIDR", buySpreadUsdToIdr);
        cached.put("calculationOwner", githubUname);
        cached.put("spreadFactor", spreadFactor);

        cachedData.set(cached);
    }

    private double calculateSpreadFactor() {
        int sum = IdrLatestRateStrategyImpl.githubUname.toLowerCase().chars().sum();
        return (sum % 1000) / 100000.0;
    }
}
