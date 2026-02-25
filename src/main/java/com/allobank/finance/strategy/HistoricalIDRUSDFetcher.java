package com.allobank.finance.strategy;

import com.allobank.finance.client.model.HistoricalRate;
import com.allobank.finance.model.HistoricalRateData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoricalIDRUSDFetcher implements IDRDataFetcher {

    private final RestClient restClient;

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public HistoricalRateData fetchData() {
        log.debug("Fetching historical IDR-USD rates");

        HistoricalRate response = restClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .body(HistoricalRate.class);

        Assert.notNull(response, "Failed to fetch historical IDR-USD rates");

        return HistoricalRateData.builder()
                .amount(response.amount())
                .base(response.base())
                .startDate(response.startDate())
                .endDate(response.endDate())
                .rates(response.rates())
                .build();
    }
}
