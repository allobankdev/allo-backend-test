package com.allobank.finance.idrrateaggregator.startegy.impl;

import com.allobank.finance.idrrateaggregator.client.FrankfurterClient;
import com.allobank.finance.idrrateaggregator.startegy.IDRDataFetcher;
import com.allobank.finance.idrrateaggregator.model.dto.FinanceResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public List<FinanceResponse> fetch() {

        Map<String, String> currencies =
                Optional.ofNullable(client.getSupportedCurrencies())
                        .orElseThrow(() ->
                                new IllegalStateException("Supported currencies not available")
                        );

        return currencies.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry ->
                        new FinanceResponse(
                                entry.getKey(),
                                entry.getValue()
                        )
                )
                .toList();
    }
}
