package com.allo.finance.strategy;

public interface IDRDataFetcher {
    String resourceType();
    Object fetch();
}
