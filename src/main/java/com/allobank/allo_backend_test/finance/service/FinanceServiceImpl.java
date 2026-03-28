package com.allobank.allo_backend_test.finance.service;

import com.allobank.allo_backend_test.finance.exception.ResourceNotFoundException;
import com.allobank.allo_backend_test.finance.exception.ResourceNotSupportedException;
import com.allobank.allo_backend_test.finance.exception.ServiceUnavailableException;
import com.allobank.allo_backend_test.finance.model.FinanceResource;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;
import com.allobank.allo_backend_test.finance.service.strategy.FinanceResourceRegistry;
import com.allobank.allo_backend_test.finance.service.strategy.finance_resource.FinanceResourceHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceService {
    private final FinanceResourceRegistry registry;

    @Override
    public FinanceResource fetchByResourceType(String resourceType) {
        FinanceResourceHandler handler = registry.get(resourceType);
        if (handler == null) {
            throw new ResourceNotSupportedException("ResourceNotSupportedException: " + resourceType);
        }
        return handler.fetch();
    }

    @Override
    public FinanceResource findByResourceType(String resourceType) {
        FinanceResourceHandler handler = registry.get(resourceType);
        if (handler == null) {
            throw new ResourceNotSupportedException("ResourceNotSupportedException: " + resourceType);
        }

        FinanceResource resource = handler.get();
        if (resource == null) {
            throw new ResourceNotFoundException("ResourceNotFoundException: " + resourceType);
        }

        return resource;
    }
}