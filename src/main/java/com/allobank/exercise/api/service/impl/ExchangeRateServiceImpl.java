package com.allobank.exercise.api.service.impl;

import com.allobank.exercise.api.dto.CurrencyInfo;
import com.allobank.exercise.api.integration.FrankfurterClient;
import com.allobank.exercise.api.service.ExchangeRateService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final FrankfurterClient frankfurterClient;

    public ExchangeRateServiceImpl(FrankfurterClient frankfurterClient) {
        this.frankfurterClient = frankfurterClient;
    }

    @Override
    public Object getLatestRates() {
        return null;
    }

    @Override
    public Object getHistoricalRates() {
        return null;
    }

    @Override
    public List<CurrencyInfo> getSupportedCurrencies() {
        LinkedHashMap <String, String> currencyResponse =  frankfurterClient.getSupportedCurrencies();
        List<CurrencyInfo> result = currencyResponse.entrySet().stream()
                .map(e -> new CurrencyInfo(e.getKey(), e.getValue()))
                .toList();

        return result;
    }
}
