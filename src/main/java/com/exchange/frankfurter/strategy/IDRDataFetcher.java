package com.exchange.frankfurter.strategy;

public interface IDRDataFetcher {

    String getResourceType();

    Object fetchData();
}
