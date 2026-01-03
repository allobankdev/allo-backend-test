package com.allobank.allobackendtest.strategy;

public interface IDRDataFetcher {
    String getResourceType();
    Object fetchData();
}
