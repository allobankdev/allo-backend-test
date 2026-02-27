package com.allobank.finance.service.fetcher;

public interface IDRDataFetcher {
    String getResourceType();
    Object fetchData();
}
