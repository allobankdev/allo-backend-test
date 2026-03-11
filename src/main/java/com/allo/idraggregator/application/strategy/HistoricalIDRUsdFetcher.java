package com.allo.idraggregator.application.strategy;

import org.springframework.stereotype.Component;

import com.allo.idraggregator.domain.model.HistoricalRates;
import com.allo.idraggregator.domain.strategy.IDRDataFetcher;
import com.allo.idraggregator.infrastructure.client.FrankfurterClient;
import com.allo.idraggregator.infrastructure.config.properties.FrankfurterProperties;

import lombok.RequiredArgsConstructor;

@Component("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalIDRUsdFetcher implements IDRDataFetcher<HistoricalRates> {

    private final FrankfurterClient client;
    private final FrankfurterProperties properties;

    @Override
    public HistoricalRates fetchData() {

        return client.getHistorical(properties.historicalRange(), "IDR", "USD");

    }
}