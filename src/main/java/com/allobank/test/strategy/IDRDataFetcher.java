package com.allobank.test.strategy;

import java.util.List;
import java.util.Map;

public interface IDRDataFetcher {

    String resourceType();

    List<Map<String, Object>> fetch();
}
