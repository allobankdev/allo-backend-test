package com.example.AlloBank.loader;

import com.example.AlloBank.client.FrankfurterClient;
import com.example.AlloBank.response.CurrenciesResponse;
import com.example.AlloBank.response.HistoricalRatesResponse;
import com.example.AlloBank.response.LatestRatesResponse;
import com.example.AlloBank.store.FinanceStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupDataLoader implements ApplicationRunner {

    private final FrankfurterClient frankfurterClient;
    private final FinanceStore financeStore;

    public StartupDataLoader(FrankfurterClient frankfurterClient,
                             FinanceStore financeStore) {
        this.frankfurterClient = frankfurterClient;
        this.financeStore = financeStore;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LatestRatesResponse latestRates = frankfurterClient.getLatestRates();
        HistoricalRatesResponse historicalRates = frankfurterClient.getHistoricalUsd();
        CurrenciesResponse currencies = frankfurterClient.getCurrencies();
        financeStore.initialize(latestRates, historicalRates, currencies);
    }
}
