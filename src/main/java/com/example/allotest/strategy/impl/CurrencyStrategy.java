package com.example.allotest.strategy.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.allotest.dto.CurrencyResponse;
import com.example.allotest.service.DataStoreService;
import com.example.allotest.strategy.IDRDataFetcher;

@Component("supported_currencies")
public class CurrencyStrategy implements IDRDataFetcher {
    private final DataStoreService store;

    public CurrencyStrategy(DataStoreService store) {
        this.store = store;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getData() {
        Map<String, String> data = (Map<String, String>) store.get("supported_currencies");
        CurrencyResponse response = new CurrencyResponse();
        response.setCurencies(data);
        return response;
    }
}
