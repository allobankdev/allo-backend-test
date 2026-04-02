package com.allobank.finance.strategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IDRDataFetcher {

    void fetchAndCacheData();

    Optional<List<Map<String, Object>>> getData();

    String getResourceType();
}
