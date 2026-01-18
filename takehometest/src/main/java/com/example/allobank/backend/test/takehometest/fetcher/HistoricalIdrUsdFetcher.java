package com.example.allobank.backend.test.takehometest.fetcher;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.allobank.backend.test.takehometest.client.FrankfurterClient;

@Component
public class HistoricalIdrUsdFetcher implements DataFetcher {

    @Value("${app.historical.from-date}")
    private String historyFromDate;

    @Value("${app.historical.to-date}")
    private String historyToDate;

    @Value("${app.historical.base}")
    private String historyBase;

    @Value("${app.historical.target}")
    private String historyTarget;

    private FrankfurterClient client;

    public HistoricalIdrUsdFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public List<Object> fetchData() {
        return List.of(client.getHistoricalIdrUsd(
                this.historyFromDate,
                this.historyToDate,
                this.historyBase,
                this.historyTarget));
    }
}
