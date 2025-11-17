package com.allobank.exercise.api.service.impl;

import com.allobank.exercise.api.service.IDRDataFetcher;
import org.springframework.stereotype.Service;

@Service("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {
    @Override
    public Object getData() {
        return null;
    }
}
