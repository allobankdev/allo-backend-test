package com.allobank.allo_backend_test.finance.service.strategy.finance_resource;

import com.allobank.allo_backend_test.finance.model.FinanceResource;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;

public abstract class AbstractFinanceResourceHandler implements FinanceResourceHandler {

    protected final FinanceRepository repository;

    protected AbstractFinanceResourceHandler(FinanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public FinanceResource get() {
        return repository.get(resourceType());
    }
}