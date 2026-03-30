package com.allobank.idrrates.strategy;

public interface IdrDataFetcher {
    String getResourceType();
    Object fetchData();
}
