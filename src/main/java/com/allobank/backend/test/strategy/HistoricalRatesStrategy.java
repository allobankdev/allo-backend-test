package com.allobank.backend.test.strategy;

import com.allobank.backend.test.model.ApiResult;
import com.allobank.backend.test.model.DataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalRatesStrategy implements DataStrategy {

    private final DataStore store;

    @Override
    public ApiResult execute() {
        return new ApiResult("historical_idr_usd", store.getHistoricalRates());
    }
}