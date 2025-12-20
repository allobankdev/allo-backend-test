package com.allo_backend_test.strategy;

import com.allo_backend_test.dto.FinanceResponseDto;

import java.util.List;

public interface IDRDataFetcher {

    String resourceType();
    List<FinanceResponseDto> fetch();

}
