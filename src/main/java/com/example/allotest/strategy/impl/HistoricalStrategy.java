package com.example.allotest.strategy.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.allotest.dto.HistoricalResponse;
import com.example.allotest.service.DataStoreService;
import com.example.allotest.strategy.IDRDataFetcher;

@Component("historical_idr_usd")
public class HistoricalStrategy implements IDRDataFetcher {

    private final DataStoreService store;

    public HistoricalStrategy(DataStoreService store) {
        this.store = store;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getData() {
        Map<String, Object> data = (Map<String, Object>) store.get("historical_idr_usd");
        HistoricalResponse response = new HistoricalResponse();
        response.setBase((String) data.get("base"));
        response.setRetes((Map<String, Map<String, Double>>) data.get("rates"));
        return response;
    }
}
