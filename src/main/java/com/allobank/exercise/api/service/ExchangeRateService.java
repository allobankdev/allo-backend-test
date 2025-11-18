package com.allobank.exercise.api.service;

import com.allobank.exercise.api.dto.CurrencyInfo;

import java.util.List;

public interface ExchangeRateService {
    Object getLatestRates();
    Object getHistoricalRates();
    List<CurrencyInfo> getSupportedCurrencies();
}
