package com.interview.backend.strategy;

import java.util.Map;

public interface IDRDataFetcher {

    Map<String, Object> fetchData();

    default Map<String, Object> fetchData(Map<String, String> params) {
        return fetchData();
    }

    String getResourceType();
}
