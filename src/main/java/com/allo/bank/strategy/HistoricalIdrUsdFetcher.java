package com.allo.bank.strategy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.allo.bank.client.FrankfurterClient;
import com.allo.bank.client.dto.FrankfurterHistoricalResponse;
import com.allo.bank.dto.FinanceDataItem;

@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    public static final String RESOURCE_TYPE = "historical_idr_usd";

    private final FrankfurterClient frankfurterClient;

    public HistoricalIdrUsdFetcher(FrankfurterClient frankfurterClient) {
        this.frankfurterClient = frankfurterClient;
    }

    @Override
    public String resourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public List<FinanceDataItem> fetch() {
        FrankfurterHistoricalResponse response = frankfurterClient.fetchHistoricalIdrUsd();

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("base", response.getBase());
        payload.put("amount", response.getAmount());
        payload.put("rates", response.getRates() == null ? Map.of() : Map.copyOf(response.getRates()));

        return List.of(new FinanceDataItem(resourceType(), Map.copyOf(payload)));
    }
}
