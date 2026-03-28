package com.allobank.allo_backend_test.finance.service;

import com.allobank.allo_backend_test.finance.model.FinanceResource;

public interface FinanceService {
    FinanceResource fetchByResourceType(String resourceType);
    FinanceResource findByResourceType(String resourceType);
}