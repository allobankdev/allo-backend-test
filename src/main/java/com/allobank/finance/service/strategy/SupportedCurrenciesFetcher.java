package com.allobank.finance.service.strategy;

import com.allobank.finance.integration.frankfurter.FrankfurterClient;
import com.allobank.finance.model.response.SupportedCurrenciesResponseBuilder;
import com.allobank.finance.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component(Constants.SUPPORTED_CURRENCIES)
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final FrankfurterClient client;

    @Override
    public String getResourceType() {
        return Constants.SUPPORTED_CURRENCIES;
    }

    @Override
    public Object fetchData() {
        var raw = client.fetchSupportedCurrencies();

        return raw.currencies().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> SupportedCurrenciesResponseBuilder.builder()
                                .code(e.getKey())
                                .name(e.getValue())
                                .build()
                )
                .toList();
    }
}
