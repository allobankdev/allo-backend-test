package com.allobank.finance.service.fetcher.impl;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.dto.HistoricalIdrUsdDto;
import com.allobank.finance.enums.ResourceType;
import com.allobank.finance.service.fetcher.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final FrankfurterClient frankfurterClient;

    @Override
    public String getResourceType() {
        return ResourceType.HISTORICAL_IDR_USD.getValue();
    }

    @Override
    public HistoricalIdrUsdDto fetchData() {
        return frankfurterClient.getHistoricalRates();
    }
}
