package com.allo_backend_test.strategy;

import com.allo_backend_test.client.FrankfurterApiClient;
import com.allo_backend_test.dto.FinanceResponseDto;
import com.allo_backend_test.dto.HistoricalRatesResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class HistoricalIdrUsdStrategy implements IDRDataFetcher{

    private final FrankfurterApiClient apiClient;

    public HistoricalIdrUsdStrategy(FrankfurterApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public String resourceType() {
        return ResourceTypes.HISTORICAL_IDR_USD;
    }

    @Override
    public List<FinanceResponseDto> fetch() {
        HistoricalRatesResponse response = apiClient.fetchHistoricalIdrUsd();

        return response.getRates().entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // SORT DATE
                .map(entry -> {
                    String date = entry.getKey();
                    Double usdRate = entry.getValue().get("USD");
                    return new FinanceResponseDto(date, usdRate);
                })
                .toList();
    }

}
