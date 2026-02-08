package com.allobank.idr.strategy;

import java.util.Map;

public interface IDRDataFetcher {
    String getResourceType();
    Map<String, Object> fetchData();
}
