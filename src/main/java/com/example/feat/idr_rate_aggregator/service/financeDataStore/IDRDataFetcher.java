package com.example.feat.idr_rate_aggregator.service.financeDataStore;

public interface IDRDataFetcher {
    String getResourceKey();
    Object fetchData();
}
