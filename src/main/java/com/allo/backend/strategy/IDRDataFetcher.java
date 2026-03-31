package com.allo.backend.strategy;

public interface IDRDataFetcher {
    String getType();

    Object fetchData();
}
