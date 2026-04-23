package com.allobank.financeaggregator.strategy;

import com.allobank.financeaggregator.dto.SupportedCurrenciesDto;
import com.allobank.financeaggregator.service.FrankfurterClient;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private static final ParameterizedTypeReference<Map<String, String>> CURRENCY_MAP
            = new ParameterizedTypeReference<>() {};

    private final FrankfurterClient client;

    public SupportedCurrenciesFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public SupportedCurrenciesDto fetchData() {
        Map<String, String> response = client.get("/currencies", CURRENCY_MAP);
        return new SupportedCurrenciesDto(Map.copyOf(response));
    }
}
