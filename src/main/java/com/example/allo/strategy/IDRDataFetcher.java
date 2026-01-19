package com.example.allo.strategy;

public interface IDRDataFetcher {
    String getResourceType();
    Object fetch();
}

