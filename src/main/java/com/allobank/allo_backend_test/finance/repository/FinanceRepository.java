package com.allobank.allo_backend_test.finance.repository;

import com.allobank.allo_backend_test.finance.model.FinanceResource;

import java.util.Map;

public interface FinanceRepository {
    void put(String resourceType, FinanceResource resource);
    FinanceResource get(String resourceType);
    Map<String, FinanceResource> getData();
}