package com.allo.backend.strategy;

public interface IDRDataFetcher {

    String getResourceType();

    Object fetchData();
}
