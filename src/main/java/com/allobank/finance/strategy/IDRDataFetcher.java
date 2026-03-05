package com.allobank.finance.strategy;

import java.util.List;
import java.util.Map;

public interface IDRDataFetcher {

    List<Map<String, Object>> fetch();

    String getResourceType();
}
