package com.finance.service;

import java.util.List;
import java.util.Map;

// IDRDataFetcher.java
public interface IDRDataFetcher {
    String resourceType();
    List<Map<String, Object>> fetch();
}
