package com.musfiul.idrrateaggregator.service.fetcher.impl;

import com.musfiul.idrrateaggregator.client.FrankfurterClient;
import com.musfiul.idrrateaggregator.constant.ResourceType;
import com.musfiul.idrrateaggregator.dto.HistoricalRatesResponse;
import com.musfiul.idrrateaggregator.service.fetcher.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoricalIdrUsdFetcherImpl implements IDRDataFetcher {
    private static final String HISTORICAL_IDR_USD = "historical_idr_usd";
    private final FrankfurterClient client;
    @Override
    public ResourceType getType() {
        return ResourceType.from(HISTORICAL_IDR_USD);
    }

    @Override
    public HistoricalRatesResponse fetchData() {
        return client.getHistoricalRates();
    }
}
