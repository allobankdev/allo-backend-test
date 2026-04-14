package com.example.frankfurter.strategy;

import java.util.List;

public interface IDRDataFetcher {

    /**
     * Resource type that this strategy supports,
     * e.g. "latest_idr_rates", "historical_idr_usd", "supported_currencies"
     */
    String getResourceType();

    /**
     * Fetch and transform data.
     * Should always return a List<?> (unified JSON array response).
     */
    List<?> fetchData();
}
