package com.finance.aggregator.service;

import java.util.Map;

public interface DataStoreService {
    void simpanData(String resourceType, Object data);
    Object ambilData(String resourceType);
    boolean isDataLengkap();
    Map<String, Object> getAllData();
    boolean isLoaded();
}