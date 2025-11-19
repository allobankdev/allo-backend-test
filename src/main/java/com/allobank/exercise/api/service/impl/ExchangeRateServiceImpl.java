package com.allobank.exercise.api.service.impl;

import com.allobank.exercise.api.integration.FrankfurterClient;
import com.allobank.exercise.api.integration.dto.ExchangeHistoryResponse;
import com.allobank.exercise.api.integration.dto.ExchangeRateResponse;
import com.allobank.exercise.api.service.ExchangeRateService;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final FrankfurterClient frankfurterClient;

    public ExchangeRateServiceImpl(FrankfurterClient frankfurterClient) {
        this.frankfurterClient = frankfurterClient;
    }

    @Override
    public ExchangeRateResponse getLatestRates() {
        return frankfurterClient.getLatestRates();
    }

    @Override
    public ExchangeHistoryResponse getExchangeHistory(String queryTime, String fromCurrency, String toCurrency) {
        return frankfurterClient.getExchangeHistory(queryTime, fromCurrency, toCurrency);
    }

    @Override
    public Map<String, String> getSupportedCurrencies() {
        return frankfurterClient.getSupportedCurrencies();
    }
}
