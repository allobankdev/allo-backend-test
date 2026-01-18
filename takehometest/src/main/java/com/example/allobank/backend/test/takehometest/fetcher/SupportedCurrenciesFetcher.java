package com.example.allobank.backend.test.takehometest.fetcher;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.allobank.backend.test.takehometest.client.FrankfurterClient;

@Component
public class SupportedCurrenciesFetcher implements DataFetcher {

    private final FrankfurterClient client;

    public SupportedCurrenciesFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public List<Object> fetchData() {
        return List.of(client.getSupporCurrencies());
    }
}
