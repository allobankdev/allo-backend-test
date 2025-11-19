package com.finance.service;

import java.util.List;
import java.util.Map;

public interface DataFetcher {
    String resourceType();
    List<Map<String, Object>> fetch();
}
