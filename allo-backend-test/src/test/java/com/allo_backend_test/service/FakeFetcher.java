package com.allo_backend_test.service;

import com.allo_backend_test.dto.FinanceResponseDto;
import com.allo_backend_test.strategy.IDRDataFetcher;

import java.util.List;

public class FakeFetcher implements IDRDataFetcher {

    private final String resourceType;

    public FakeFetcher(String resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public String resourceType() {
        return resourceType;
    }

    @Override
    public List<FinanceResponseDto> fetch() {
        return List.of(
                new FinanceResponseDto("USD", 0.00006)
        );
    }

}
