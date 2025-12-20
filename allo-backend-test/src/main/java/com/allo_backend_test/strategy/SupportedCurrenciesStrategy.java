package com.allo_backend_test.strategy;

import com.allo_backend_test.client.FrankfurterApiClient;
import com.allo_backend_test.dto.FinanceResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SupportedCurrenciesStrategy implements IDRDataFetcher{

    private final FrankfurterApiClient apiClient;

    public SupportedCurrenciesStrategy(FrankfurterApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public String resourceType() {
        return ResourceTypes.SUPPORTED_CURRENCIES;
    }

    @Override
    public List<FinanceResponseDto> fetch() {
        Map<String, String> currencies = apiClient.fetchSupportedCurrencies();

        List<FinanceResponseDto> result = new ArrayList<>();

        for (Map.Entry<String, String> entry : currencies.entrySet()) {
            result.add(new FinanceResponseDto(entry.getKey(), entry.getValue()));
        }

        return result;
    }

}
