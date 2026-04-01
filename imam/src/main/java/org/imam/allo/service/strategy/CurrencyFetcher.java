package org.imam.allo.service.strategy;

import org.imam.allo.client.FrankfurterClient;
import org.springframework.stereotype.Service;

@Service
public class CurrencyFetcher implements IDRDataFetcher{
    private final FrankfurterClient client;

    public CurrencyFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetchData() {
        return client.getCurrencies();
    }
}
