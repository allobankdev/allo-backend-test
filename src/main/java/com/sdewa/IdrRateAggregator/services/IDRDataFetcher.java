package com.sdewa.IdrRateAggregator.services;

public interface IDRDataFetcher<T> {
    T fetchData();

    String getResourceType();
}
