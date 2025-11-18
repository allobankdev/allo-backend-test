package com.allobank.exercise.api.service.impl;

import com.allobank.exercise.api.service.IDRDataFetcher;
import org.springframework.stereotype.Service;

@Service("supported_currencies")
public class SupportedCurrencyFetcher implements IDRDataFetcher {
    @Override
    public Object getData() {
        return null;
    }
}
