package com.allobank.allobank_api.strategy;

public interface IDRDataFetcher<T> {
    String getType();
    T fetchAndTransform();
}
