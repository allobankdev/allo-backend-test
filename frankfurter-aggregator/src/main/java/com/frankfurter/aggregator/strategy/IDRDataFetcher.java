package com.frankfurter.aggregator.strategy;

import com.frankfurter.aggregator.dto.internal.FinanceDataResponse;

public interface IDRDataFetcher {
    String getResourceType();
    FinanceDataResponse fetchData();  
}
