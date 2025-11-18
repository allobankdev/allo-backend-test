package com.allo.test.modules.finance.loader;

import com.allo.test.modules.finance.client.FrankfurterClient;
import com.allo.test.modules.finance.dto.res.CurrenciesResponse;
import com.allo.test.modules.finance.dto.res.HistoricalRatesResponse;
import com.allo.test.modules.finance.dto.res.LatestRatesResponse;
import com.allo.test.modules.finance.store.FrankfurterDataStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FrankfurterDataLoader implements ApplicationRunner {

    private final FrankfurterClient frankfurterClient;
    private final FrankfurterDataStore dataStore;

    public FrankfurterDataLoader(FrankfurterClient frankfurterClient, FrankfurterDataStore dataStore) {
        this.frankfurterClient = frankfurterClient;
        this.dataStore = dataStore;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("=== Starting Frankfurter API data loading at application startup ===");

        try {
            // Fetch and store latest rates
            log.info("Loading latest exchange rates...");
            LatestRatesResponse latestRates = frankfurterClient.fetchLatestRates();
            if (latestRates != null) {
                dataStore.storeLatestRates(latestRates);
                log.info("Latest rates loaded successfully for base currency: {}", latestRates.getBase());
            } else {
                log.warn("Failed to load latest rates - response was null");
            }

            // Fetch and store historical rates
            log.info("Loading historical exchange rates...");
            HistoricalRatesResponse historicalRates = frankfurterClient.fetchHistoricalRates();
            if (historicalRates != null) {
                dataStore.storeHistoricalRates(historicalRates);
                log.info("Historical rates loaded successfully for base currency: {}", historicalRates.getBase());
            } else {
                log.warn("Failed to load historical rates - response was null");
            }

            // Fetch and store currencies
            log.info("Loading supported currencies...");
            CurrenciesResponse currencies = frankfurterClient.fetchCurrencies();
            if (currencies != null) {
                dataStore.storeCurrencies(currencies);
                log.info("Currencies loaded successfully - {} currencies available",
                        currencies.getCurrencies() != null ? currencies.getCurrencies().size() : 0);
            } else {
                log.warn("Failed to load currencies - response was null");
            }

            // Verify all data is loaded
            if (dataStore.isDataLoaded()) {
                log.info("=== Frankfurter API data loading completed successfully ===");
            } else {
                log.error("=== Frankfurter API data loading incomplete - some resources failed to load ===");
            }

        } catch (Exception e) {
            log.error("Error occurred while loading Frankfurter API data: {}", e.getMessage());
            log.warn("=== Application will start with empty data store. Data can be loaded later when API is available ===");
        }
    }
}
