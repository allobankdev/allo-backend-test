package com.allobank.financeapi.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class DataStore {

    @Getter
    @Setter
    private List<LatestIdrWithSpread> latestIdrRates;

    @Getter
    @Setter
    private Map<String, Object> historicalIdrUsd;

    @Getter
    @Setter
    private Map<String, String> supportedCurrencies;

    @Getter
    @Setter
    private volatile boolean initialized = false;

    public synchronized void setAllData(
            List<LatestIdrWithSpread> latest,
            Map<String, Object> historical,
            Map<String, String> currencies) {
        this.latestIdrRates = List.copyOf(latest);
        this.historicalIdrUsd = Map.copyOf(historical);
        this.supportedCurrencies = Map.copyOf(currencies);
        this.initialized = true;
    }
}