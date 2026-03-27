package com.allobank.allo_backend_test.finance.repository;

import com.allobank.allo_backend_test.finance.model.FinanceResource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FinanceRepositoryImpl implements FinanceRepository {

    private Map<String, FinanceResource> data = new HashMap<>();

    @Override
    public void put(String resourceType, FinanceResource resource) {
        data.put(resourceType, resource);
    }

    @Override
    public FinanceResource get(String resourceType) {
        return data.get(resourceType);
    }

    @Override
    public Map<String, FinanceResource> getData() {
        return data;
    }
}