package com.example.AlloBank.store;

import com.example.AlloBank.response.CurrenciesResponse;
import com.example.AlloBank.response.HistoricalRatesResponse;
import com.example.AlloBank.response.LatestRatesResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class FinanceStore {

    private LatestRatesResponse latestRates;
    private HistoricalRatesResponse historicalRates;
    private CurrenciesResponse currencies;

    private boolean initialized = false;

    public synchronized void initialize(
            LatestRatesResponse latestRates,
            HistoricalRatesResponse historicalRates,
            CurrenciesResponse currencies
    ) {
        if (initialized) {
            throw new IllegalStateException("FinanceStore already initialized");
        }

        this.latestRates = copyLatestRates(latestRates);
        this.historicalRates = copyHistoricalRates(historicalRates);
        this.currencies = copyCurrencies(currencies);

        this.initialized = true;
    }

    public LatestRatesResponse getLatestRates() {
        return Objects.requireNonNull(latestRates, "Store not initialized");
    }

    public HistoricalRatesResponse getHistoricalRates() {
        return Objects.requireNonNull(historicalRates, "Store not initialized");
    }

    public CurrenciesResponse getCurrencies() {
        return Objects.requireNonNull(currencies, "Store not initialized");
    }


    private LatestRatesResponse copyLatestRates(LatestRatesResponse src) {
        LatestRatesResponse copy = new LatestRatesResponse();
        copy.setAmount(src.getAmount());
        copy.setBase(src.getBase());
        copy.setDate(src.getDate());
        copy.setRates(Map.copyOf(src.getRates()));
        return copy;
    }

    private HistoricalRatesResponse copyHistoricalRates(HistoricalRatesResponse src) {
        HistoricalRatesResponse copy = new HistoricalRatesResponse();
        copy.setAmount(src.getAmount());
        copy.setBase(src.getBase());
        copy.setStart_date(src.getStart_date());
        copy.setEnd_date(src.getEnd_date());
        copy.setRates(
                src.getRates().entrySet().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                e -> e.getKey(),
                                e -> Map.copyOf(e.getValue())
                        ))
        );
        return copy;
    }

    private CurrenciesResponse copyCurrencies(CurrenciesResponse src) {
        CurrenciesResponse copy = new CurrenciesResponse();
        if (src.getCurrencies() != null) {
            copy.setCurrencies(Map.copyOf(src.getCurrencies()));
        } else {
            copy.setCurrencies(Map.of());
        }
        return copy;
    }

}
