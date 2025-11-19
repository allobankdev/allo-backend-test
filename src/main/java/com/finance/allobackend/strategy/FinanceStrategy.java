package com.finance.allobackend.strategy;

import org.springframework.web.client.RestTemplate;

public interface FinanceStrategy {
    String getResourceType();
    Object getCacheData();
    void getOrRefreshData(RestTemplate restTemplate);
}
