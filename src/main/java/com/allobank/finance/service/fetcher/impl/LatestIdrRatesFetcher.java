package com.allobank.finance.service.fetcher.impl;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.dto.LatestIdrRatesDto;
import com.allobank.finance.enums.ResourceType;
import com.allobank.finance.service.fetcher.IDRDataFetcher;
import com.allobank.finance.util.SpreadCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final FrankfurterClient frankfurterClient;

    @Value("${github.username}")
    private String githubUsername;

    @Override
    public String getResourceType() {
        return ResourceType.LATEST_IDR_RATES.getValue();
    }

    @Override
    public LatestIdrRatesDto fetchData() {
        LatestIdrRatesDto dto = frankfurterClient.getLatestRates();

        if (dto != null && dto.getRates() != null && dto.getRates().get("USD") != null) {
            double spread = SpreadCalculator.calculateUsdBuySpread(dto.getRates().get("USD"), githubUsername);
            dto.setUsdBuySpreadIdr(spread);
        }

        return dto;
    }
}