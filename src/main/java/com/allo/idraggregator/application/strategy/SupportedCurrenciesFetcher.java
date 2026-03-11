package com.allo.idraggregator.application.strategy;

import org.springframework.stereotype.Component;

import com.allo.idraggregator.domain.model.Currency;
import com.allo.idraggregator.domain.strategy.IDRDataFetcher;
import com.allo.idraggregator.infrastructure.client.FrankfurterClient;

import lombok.RequiredArgsConstructor;

@Component("supported_currencies")
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher<Currency> {

    private final FrankfurterClient client;

    @Override
    public Currency fetchData() {

        return client.getCurrencies();
        
    }
}
