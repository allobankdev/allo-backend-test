package com.allobank.backend.test.strategy;

import com.allobank.backend.test.model.ApiResult;
import com.allobank.backend.test.model.DataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("supported_currencies")
@RequiredArgsConstructor
public class CurrenciesStrategy implements DataStrategy {

    private final DataStore store;

    @Override
    public ApiResult execute() {
        return new ApiResult("supported_currencies", store.getCurrencies());
    }
}