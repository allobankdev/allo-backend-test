package com.allo.test.modules.finance.store;

import com.allo.test.modules.finance.dto.res.CurrenciesResponse;
import com.allo.test.modules.finance.dto.res.HistoricalRatesResponse;
import com.allo.test.modules.finance.dto.res.LatestRatesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class FrankfurterDataStore {

    private static final String LATEST_RATES_KEY = "LATEST_RATES";
    private static final String HISTORICAL_RATES_KEY = "HISTORICAL_RATES";
    private static final String CURRENCIES_KEY = "CURRENCIES";

    private final ConcurrentHashMap<String, Object> dataStore = new ConcurrentHashMap<>();

    /**
     * Stores the latest exchange rates in memory.
     *
     * @param latestRates the latest rates response to store
     */
    public void storeLatestRates(LatestRatesResponse latestRates) {
        log.info("Storing latest rates in memory");
        dataStore.put(LATEST_RATES_KEY, latestRates);
    }

    /**
     * Retrieves the latest exchange rates from memory.
     *
     * @return LatestRatesResponse or null if not available
     */
    public LatestRatesResponse getLatestRates() {
        return (LatestRatesResponse) dataStore.get(LATEST_RATES_KEY);
    }

    /**
     * Stores historical exchange rates in memory.
     *
     * @param historicalRates the historical rates response to store
     */
    public void storeHistoricalRates(HistoricalRatesResponse historicalRates) {
        log.info("Storing historical rates in memory");
        dataStore.put(HISTORICAL_RATES_KEY, historicalRates);
    }

    /**
     * Retrieves historical exchange rates from memory.
     *
     * @return HistoricalRatesResponse or null if not available
     */
    public HistoricalRatesResponse getHistoricalRates() {
        return (HistoricalRatesResponse) dataStore.get(HISTORICAL_RATES_KEY);
    }

    /**
     * Stores the list of supported currencies in memory.
     *
     * @param currencies the currencies response to store
     */
    public void storeCurrencies(CurrenciesResponse currencies) {
        log.info("Storing currencies in memory");
        dataStore.put(CURRENCIES_KEY, currencies);
    }

    /**
     * Retrieves the list of supported currencies from memory.
     *
     * @return CurrenciesResponse or null if not available
     */
    public CurrenciesResponse getCurrencies() {
        return (CurrenciesResponse) dataStore.get(CURRENCIES_KEY);
    }

    /**
     * Checks if all three resources have been loaded into the store.
     *
     * @return true if all data is available, false otherwise
     */
    public boolean isDataLoaded() {
        return dataStore.containsKey(LATEST_RATES_KEY)
                && dataStore.containsKey(HISTORICAL_RATES_KEY)
                && dataStore.containsKey(CURRENCIES_KEY);
    }

    /**
     * Clears all stored data.
     */
    public void clearAll() {
        log.warn("Clearing all stored Frankfurter data");
        dataStore.clear();
    }
}
