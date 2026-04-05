package com.example.finance.strategy;

public interface IDRDataFetcher {

    String getType();

    Object fetchData();
}