package com.allobank.allo_backend_test.finance.service.strategy.finance_resource;

import com.allobank.allo_backend_test.finance.exception.ServiceUnavailableException;
import com.allobank.allo_backend_test.finance.model.FinanceResource;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;

public abstract class AbstractFinanceResourceHandler implements FinanceResourceHandler {

    protected final FinanceRepository repository;

    protected AbstractFinanceResourceHandler(FinanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public FinanceResource get() {
        var data = repository.getData();
        if (data == null || data.isEmpty()) {
            throw new ServiceUnavailableException("Frankfurter is unavailable!");
        }
        return repository.get(resourceType());
    }
}