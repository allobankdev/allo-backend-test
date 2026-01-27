package com.backend.allobank.strategy;

public interface IDRDataFetcher {

    String getResourceType();
    Object fetchAndTransform();

}
