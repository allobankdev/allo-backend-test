package com.app.service;

import com.app.model.ExchangeAggregator;

public interface ExchangeAggregatorService {

    void save(ExchangeAggregator exchangeAggregator);
    ExchangeAggregator findById(String id);

}
