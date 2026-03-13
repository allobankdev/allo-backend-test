package com.allobank.backend.test.strategy;

import com.allobank.backend.test.model.ApiResult;
import com.allobank.backend.test.model.DataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("latest_idr_rates")
@RequiredArgsConstructor
public class LatestRatesStrategy implements DataStrategy {

    private final DataStore store;

    @Override
    public ApiResult execute() {
        return new ApiResult("latest_idr_rates", store.getLatestRates());
    }
}