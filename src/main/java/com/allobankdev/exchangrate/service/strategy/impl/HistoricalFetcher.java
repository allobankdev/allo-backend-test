package com.allobankdev.exchangrate.service.strategy.impl;

import com.allobankdev.exchangrate.client.ApiClient;
import com.allobankdev.exchangrate.service.strategy.IdrDataFetcher;
import org.springframework.stereotype.Service;

@Service
public class HistoricalFetcher implements IdrDataFetcher {
    private final ApiClient client;
    private final static String TYPE = "historical_idr_usd";

    public HistoricalFetcher (ApiClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Object fetch() {
        return client.getHistoricalRates();
    }
}
