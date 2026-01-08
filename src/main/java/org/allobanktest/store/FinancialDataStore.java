package org.allobanktest.store;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class FinancialDataStore {
    private final AtomicReference<List<?>> latestIdrRates = new AtomicReference<>();
    private final AtomicReference<List<?>> historicalIdrUsd = new AtomicReference<>();
    private final AtomicReference<List<?>> supportedCurrencies = new AtomicReference<>();

    public void setLatestIdrRates(List<?> data) {
        latestIdrRates.compareAndSet(null, List.copyOf(data));
    }

    public void setHistoricalIdrUsd(List<?> data) {
        historicalIdrUsd.compareAndSet(null, List.copyOf(data));
    }

    public void setSupportedCurrencies(List<?> data) {
        supportedCurrencies.compareAndSet(null, List.copyOf(data));
    }

    public List<?> getLatestIdrRates() {
        return latestIdrRates.get();
    }

    public List<?> getHistoricalIdrUsd() {
        return historicalIdrUsd.get();
    }

    public List<?> getSupportedCurrencies() {
        return supportedCurrencies.get();
    }
}
