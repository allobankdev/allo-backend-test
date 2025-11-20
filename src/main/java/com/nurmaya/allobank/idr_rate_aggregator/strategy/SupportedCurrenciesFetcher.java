package com.nurmaya.allobank.idr_rate_aggregator.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nurmaya.allobank.idr_rate_aggregator.client.FrankfurterClient;
import com.nurmaya.allobank.idr_rate_aggregator.dto.CurrencyListResponse;

@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {
    private final FrankfurterClient client;

    public SupportedCurrenciesFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public List<CurrencyListResponse> fetchData() {
        CurrencyListResponse response = client.getSupportedCurrencies();
        return List.of(response);
    }
}
