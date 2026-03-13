package com.allobank.backend.test.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class DataStore {
    private LatestRatesResponse latestRates;
    private CurrenciesResponse currencies;
    private HistoricalRatesResponse historicalRates;
}