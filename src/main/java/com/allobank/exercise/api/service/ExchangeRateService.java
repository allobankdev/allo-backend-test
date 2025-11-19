package com.allobank.exercise.api.service;

import com.allobank.exercise.api.integration.dto.ExchangeHistoryResponse;

import java.util.Map;

public interface ExchangeRateService {
    Object getLatestRates();

    ExchangeHistoryResponse getExchangeHistory(String queryTime, String fromCurrency, String toCurrency);
    Map<String, String> getSupportedCurrencies();
}
