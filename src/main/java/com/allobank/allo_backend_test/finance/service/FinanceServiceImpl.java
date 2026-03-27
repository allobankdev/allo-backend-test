package com.allobank.allo_backend_test.finance.service;

import com.allobank.allo_backend_test.finance.model.FinanceResource;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceService {

    private final FinanceRepository repository;

    @Override
    public FinanceResource findByResourceType(String resourceType) {
        return repository.get(resourceType);
    }
}