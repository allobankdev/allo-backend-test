package com.allobank.assignment.strategy;

import com.allobank.assignment.client.FrankfurterApiClient;
import com.allobank.assignment.model.FinanceDataResponse;
import com.allobank.assignment.model.ResourceType;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

@Component
public class SupportedCurrenciesStrategy implements IdrDataFetchStrategy{

    private final FrankfurterApiClient apiClient;

    public SupportedCurrenciesStrategy(FrankfurterApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public ResourceType supports() {
        return ResourceType.SUPPORTED_CURRENCIES;
    }

    @Override
    public FinanceDataResponse fetch() {
        Map<String, String> currencies = apiClient.getSupportedCurrencies();
        return new FinanceDataResponse(supports().value(), currencies, Instant.now());
    }
}
