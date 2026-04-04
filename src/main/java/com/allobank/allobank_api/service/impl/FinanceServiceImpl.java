package com.allobank.allobank_api.service.impl;

import org.springframework.stereotype.Service;

import com.allobank.allobank_api.service.FinanceService;
import com.allobank.allobank_api.store.DataStore;

@Service
public class FinanceServiceImpl implements FinanceService {
    private final DataStore store;

    public FinanceServiceImpl(DataStore store) {
        this.store = store;
    }

    @Override
    public Object getData(String type) {
        Object data = store.get(type, Object.class);

        if (data == null) {
            throw new IllegalArgumentException("Invalid resource type: " + type);
        }
        return data;
    }
}
