package com.example.finance.strategy;

import java.util.List;
import java.util.Map;

public interface IDRDataFetcher {
    String getResourceType();
    List<Map<String, Object>> fetchData();
}