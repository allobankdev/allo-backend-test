package com.nurmaya.allobank.idr_rate_aggregator.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nurmaya.allobank.idr_rate_aggregator.client.FrankfurterClient;
import com.nurmaya.allobank.idr_rate_aggregator.dto.HistoricalRatesResponse;

@Component("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements IDRDataFetcher{
        private final FrankfurterClient client;

    public HistoricalIdrUsdFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public List<HistoricalRatesResponse> fetchData() {
        HistoricalRatesResponse response = client.getHistoricalIdrUsd();
        return List.of(response);
    }
}
