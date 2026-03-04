package com.idr_rate_aggregator_2.demo.idr_data_fetchers_interface;

import reactor.core.publisher.Mono;

public interface IDRDataFetcher {
    String getResourceType();
    Mono<?> fetchData();  // <-- Gunakan wildcard
    Class<?> getResponseType();  // <-- Gunakan wildcard
}