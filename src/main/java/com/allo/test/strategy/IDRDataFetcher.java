package com.allo.test.strategy;

public interface IDRDataFetcher {

    String getResourceType();

    Object fetchData();
}
