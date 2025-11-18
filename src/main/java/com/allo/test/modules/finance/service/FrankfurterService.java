package com.allo.test.modules.finance.service;

import com.allo.test.modules.finance.dto.res.CurrenciesResponse;
import com.allo.test.modules.finance.dto.res.HistoricalRatesResponse;
import com.allo.test.modules.finance.dto.res.LatestRatesResponse;
import com.allo.test.modules.finance.store.FrankfurterDataStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FrankfurterService {

    private final FrankfurterDataStore dataStore;

    /**
     * Retrieves the latest exchange rates from the in-memory store.
     *
     * @return Optional containing LatestRatesResponse if available
     */
    public Optional<LatestRatesResponse> getLatestRates() {
        log.debug("Retrieving latest rates from data store");
        return Optional.ofNullable(dataStore.getLatestRates());
    }

    /**
     * Retrieves historical exchange rates from the in-memory store.
     *
     * @return Optional containing HistoricalRatesResponse if available
     */
    public Optional<HistoricalRatesResponse> getHistoricalRates() {
        log.debug("Retrieving historical rates from data store");
        return Optional.ofNullable(dataStore.getHistoricalRates());
    }

    /**
     * Retrieves the list of supported currencies from the in-memory store.
     *
     * @return Optional containing CurrenciesResponse if available
     */
    public Optional<CurrenciesResponse> getCurrencies() {
        log.debug("Retrieving currencies from data store");
        return Optional.ofNullable(dataStore.getCurrencies());
    }

    /**
     * Gets the exchange rate for a specific currency from the latest rates.
     *
     * @param currencyCode the currency code to look up
     * @return Optional containing the exchange rate if found
     */
    public Optional<BigDecimal> getLatestRateForCurrency(String currencyCode) {
        log.debug("Looking up latest rate for currency: {}", currencyCode);
        return getLatestRates()
                .map(LatestRatesResponse::getRates)
                .flatMap(rates -> Optional.ofNullable(rates.get(currencyCode)));
    }

    /**
     * Gets the full name of a currency from the currencies list.
     *
     * @param currencyCode the currency code to look up
     * @return Optional containing the currency name if found
     */
    public Optional<String> getCurrencyName(String currencyCode) {
        log.debug("Looking up name for currency: {}", currencyCode);
        return getCurrencies()
                .map(CurrenciesResponse::getCurrencies)
                .flatMap(currencies -> Optional.ofNullable(currencies.get(currencyCode)));
    }

    /**
     * Checks if all Frankfurter data resources are loaded and available.
     *
     * @return true if all data is loaded, false otherwise
     */
    public boolean isDataAvailable() {
        return dataStore.isDataLoaded();
    }

    /**
     * Gets all available currencies as a map.
     *
     * @return Map of currency codes to currency names, or empty map if not available
     */
    public Map<String, String> getAllCurrencies() {
        return getCurrencies()
                .map(CurrenciesResponse::getCurrencies)
                .orElse(Map.of());
    }
}
