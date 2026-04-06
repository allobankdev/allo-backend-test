package com.allobank.idr_rate_aggregator.strategy;

import com.allobank.idr_rate_aggregator.model.FinanceData;

public interface IDRDataFetcher {
    FinanceData fetch();
    String getResourceType();
}
