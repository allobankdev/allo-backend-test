package com.allobank.finance.strategy;

import com.allobank.finance.client.FrankfurterClient;
import org.springframework.stereotype.Component;

@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final FrankfurterClient frankfurterClient;

    public SupportedCurrenciesFetcher(FrankfurterClient frankfurterClient) {
        this.frankfurterClient = frankfurterClient;
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetch() {
        return frankfurterClient.getCurrencies();
    }
}
