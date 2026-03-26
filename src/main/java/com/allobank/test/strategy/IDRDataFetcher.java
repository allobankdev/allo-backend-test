package com.allobank.test.strategy;

public interface IDRDataFetcher {

    String resourceType();

    Object fetch();
}
