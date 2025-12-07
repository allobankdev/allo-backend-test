package com.example.allo_bank.strategy;

public interface IDRDataFetcher {
    String getResourceName();
    Object fetchData();

    Object safeFetch();
}
