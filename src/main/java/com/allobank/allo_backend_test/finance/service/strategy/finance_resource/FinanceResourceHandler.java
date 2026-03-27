package com.allobank.allo_backend_test.finance.service.strategy.finance_resource;

import com.allobank.allo_backend_test.finance.model.FinanceResource;

public interface FinanceResourceHandler {
    String resourceType();
    FinanceResource fetch();
    FinanceResource get();
}