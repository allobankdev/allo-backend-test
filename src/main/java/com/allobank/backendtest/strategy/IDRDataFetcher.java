package com.allobank.backendtest.strategy;

public interface IDRDataFetcher {
    String getResourceType();
    Object fetchData();
}
