package com.app.strategy;

import com.app.model.ExchangeAggregator;
import com.app.service.ExchangeAggregatorService;
import org.springframework.stereotype.Component;


public interface IDRDataFetcher<T>{

    void save (ExchangeAggregator exchangeAggregator);

    String getResourceType();

    T find(String id);

    T execute();

}
