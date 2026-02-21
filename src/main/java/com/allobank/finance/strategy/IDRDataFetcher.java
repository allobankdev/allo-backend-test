package com.allobank.finance.strategy;

public interface IDRDataFetcher {
    String getResourceType();

    Object fetch();
}
