package com.allo.finance.strategy;

public interface IDRDataFetcher {

    String getResourceType(); // key: latest_idr_rates, historical_idr_usd, supported_currencies

    Object fetchData();
}
