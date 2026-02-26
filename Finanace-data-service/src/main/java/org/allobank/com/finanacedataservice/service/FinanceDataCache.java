package org.allobank.com.finanacedataservice.service;

import org.allobank.com.finanacedataservice.domain.SupportedCurrenciesResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class FinanceDataCache {
    private final AtomicReference<List<?>> latestIdrRates = new AtomicReference<>();
    private final AtomicReference<List<?>> historyIdrUsd = new AtomicReference<>();
    private final AtomicReference<List<?>> supportedCurrencies = new AtomicReference<>();

    public void setLatestIdrRates(List<?> data) {
        setOnce(latestIdrRates, data);
    }

    public List<?> getLatestIdrRate() {
        return getOrThrow(latestIdrRates, "latest_idr_rates");
    }

    public void setHistoryIdrUsd(List<?> data) {
        setOnce(historyIdrUsd, data);
    }
    public List<?> getHistoricalIdrUsd() {
        return getOrThrow(historyIdrUsd, "history_idr_usd");
    }

    public void setSupportedCurrencies(List<?> data) {
        setOnce(supportedCurrencies, data);
    }

    public List<?> getSupportedCurrencies() {
        return getOrThrow(supportedCurrencies, "supported_currencies");
    }

    private void setOnce(AtomicReference<List<?>> reference, List<?> data) {
        Objects.requireNonNull(data, "data must not be null");
        List<?> immutableCopy = List.copyOf(data);
        if (!reference.compareAndSet(null, immutableCopy)) {
            throw  new IllegalStateException("Data has already been initial");
        }
    }

    private List<?> getOrThrow(AtomicReference<List<?>> reference, String name) {
        List<?> data = reference.get();
        if (data == null) {
            throw new IllegalStateException("Data for resource " + name+ " not init");
        }
        return data;
    }



}
