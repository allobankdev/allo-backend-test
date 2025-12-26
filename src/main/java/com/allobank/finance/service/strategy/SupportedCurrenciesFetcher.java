package com.allobank.finance.service.strategy;

import com.allobank.finance.client.FrankfurterClient;
import org.springframework.stereotype.Component;



@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final FrankfurterClient client;

    public SupportedCurrenciesFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetch() {
        return client.getCurrencies();
    }
}
