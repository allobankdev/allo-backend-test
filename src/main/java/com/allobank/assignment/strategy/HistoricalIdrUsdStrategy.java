package com.allobank.assignment.strategy;

import com.allobank.assignment.client.FrankfurterApiClient;
import com.allobank.assignment.config.FrankfurterApiProperties;
import com.allobank.assignment.config.HistoricalProperties;
import com.allobank.assignment.model.FinanceDataResponse;
import com.allobank.assignment.model.HistoricalRatesResponse;
import com.allobank.assignment.model.ResourceType;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class HistoricalIdrUsdStrategy implements IdrDataFetchStrategy{

    private final FrankfurterApiClient apiClient;
    private final FrankfurterApiProperties properties;

    public HistoricalIdrUsdStrategy(FrankfurterApiClient apiClient, FrankfurterApiProperties properties) {
        this.apiClient = apiClient;
        this.properties = properties;
    }

    @Override
    public ResourceType supports() {
        return ResourceType.HISTORICAL_IDR_USD;
    }

    @Override
    public FinanceDataResponse fetch() {
        HistoricalProperties historical = properties.getHistorical();
        HistoricalRatesResponse response = apiClient.getHistoricalRates(
                historical.getFrom(),
                historical.getTo(),
                historical.getStartDate().toString(),
                historical.getEndDate().toString());

        return new FinanceDataResponse(supports().value(), response, Instant.now());
    }
}
