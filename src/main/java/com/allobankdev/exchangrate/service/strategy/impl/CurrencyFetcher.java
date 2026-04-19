package com.allobankdev.exchangrate.service.strategy.impl;

import com.allobankdev.exchangrate.client.ApiClient;
import com.allobankdev.exchangrate.service.strategy.IdrDataFetcher;
import org.springframework.stereotype.Service;

@Service
public class CurrencyFetcher implements IdrDataFetcher {
    private final ApiClient client;
    private final static String TYPE = "supported_currencies";

    public CurrencyFetcher(ApiClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Object fetch() {
        return client.getCurrencies();
    }
}
