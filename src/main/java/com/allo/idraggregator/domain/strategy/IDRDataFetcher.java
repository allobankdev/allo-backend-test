package com.allo.idraggregator.domain.strategy;

public interface IDRDataFetcher<T> {
    
    T fetchData();
}