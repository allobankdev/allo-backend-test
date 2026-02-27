package com.allobank.finance.service.strategy;

public interface IDRDataFetcher {
    String getResourceType();
    Object fetchData();
}
