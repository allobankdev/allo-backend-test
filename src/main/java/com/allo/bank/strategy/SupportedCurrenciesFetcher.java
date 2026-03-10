package com.allo.bank.strategy;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.allo.bank.client.FrankfurterClient;
import com.allo.bank.dto.FinanceDataItem;

@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    public static final String RESOURCE_TYPE = "supported_currencies";

    private final FrankfurterClient frankfurterClient;

    public SupportedCurrenciesFetcher(FrankfurterClient frankfurterClient) {
        this.frankfurterClient = frankfurterClient;
    }

    @Override
    public String resourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public List<FinanceDataItem> fetch() {
        Map<String, String> currencies = frankfurterClient.fetchSupportedCurrencies();
        return List.of(new FinanceDataItem(resourceType(), Map.copyOf(currencies)));
    }
}
