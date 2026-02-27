package com.allobank.finance.service;

import com.allobank.finance.exception.ServiceException;
import com.allobank.finance.model.response.FinanceDataResponse;
import com.allobank.finance.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FinanceDataService {

    private final FinanceDataStore store;

    public FinanceDataResponse get(String resourceType) {
        return Optional.ofNullable(store.get(resourceType))
                .orElseThrow(() -> new ServiceException(ErrorCode.INVALID_INPUT));
    }
}
