package com.allo.test.strategy;

import com.allo.test.service.ExternalApiService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component

public class CurrencyFetcher implements  IDRDataFetcher{
    private final ExternalApiService externalApiService;
    public CurrencyFetcher(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }
    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public List<Object> fetchData() {
        Map<String, String> currencies =
                externalApiService.getCurrenciesParsed();

        return List.of(currencies);
    }
}
