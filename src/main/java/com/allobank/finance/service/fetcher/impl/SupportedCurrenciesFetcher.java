package com.allobank.finance.service.fetcher.impl;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.dto.SupportedCurrenciesDto;
import com.allobank.finance.enums.ResourceType;
import com.allobank.finance.service.fetcher.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final FrankfurterClient frankfurterClient;

    @Override
    public String getResourceType() {
        return ResourceType.SUPPORTED_CURRENCIES.getValue();
    }

    @Override
    public SupportedCurrenciesDto fetchData() {
        return frankfurterClient.getSupportedCurrencies();
    }
}