package com.allobankdev.exchangrate.service.strategy.impl;

import com.allobankdev.exchangrate.client.ApiClient;
import com.allobankdev.exchangrate.constant.ResourceType;
import com.allobankdev.exchangrate.service.strategy.IdrDataFetcher;
import org.springframework.stereotype.Service;

@Service
public class CurrencyFetcher implements IdrDataFetcher {
    private final ApiClient client;

    public CurrencyFetcher(ApiClient client) {
        this.client = client;
    }

    @Override
    public ResourceType getType() {
        return ResourceType.SUPPORTED_CURRENCIES;
    }

    @Override
    public Object fetch() {
        return client.getCurrencies();
    }
}
