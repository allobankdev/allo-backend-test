package com.allobank.test.strategy;

import com.allobank.test.client.FrankfurterClient;
import org.springframework.stereotype.Component;

@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final FrankfurterClient frankfurterClient;

    public SupportedCurrenciesFetcher(FrankfurterClient frankfurterClient) {
        this.frankfurterClient = frankfurterClient;
    }

    @Override
    public String resourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetch() {
        return frankfurterClient.fetchSupportedCurrencies();
    }
}
