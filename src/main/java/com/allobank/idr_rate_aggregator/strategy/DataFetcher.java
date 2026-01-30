package com.allobank.idr_rate_aggregator.strategy;

import java.util.List;

public interface DataFetcher {
    List<?> fetchData();

    void refreshData();
}
