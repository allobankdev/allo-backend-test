package com.bezkoder.springjwt.strategy;

import com.bezkoder.springjwt.client.FrankfurterApiClient;
import com.bezkoder.springjwt.store.FinanceDataStore;
import com.bezkoder.springjwt.util.SpreadCalculator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class LatestIdrRatesStrategy implements IDRDataFetcherStrategy {

    private static final String TYPE = "latest_idr_rates";

    private final FrankfurterApiClient apiClient;
    private final FinanceDataStore store;
    private final SpreadCalculator spreadCalculator;

    private volatile List<Object> loaded = List.of();

    public LatestIdrRatesStrategy(FrankfurterApiClient apiClient,
                                 FinanceDataStore store,
                                 SpreadCalculator spreadCalculator) {
        this.apiClient = apiClient;
        this.store = store;
        this.spreadCalculator = spreadCalculator;
    }

    @Override
    public String resourceType() {
        return TYPE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadAtStartup() {
        Map<String, Object> response = apiClient.getLatestBaseIdr();

        Object ratesObj = response.get("rates");
        if (!(ratesObj instanceof Map)) {
            throw new IllegalStateException("Invalid response format from /latest?base=IDR");
        }

        Map<String, Object> rates = (Map<String, Object>) ratesObj;
        Object usdObj = rates.get("USD");
        if (!(usdObj instanceof Number)) {
            throw new IllegalStateException("USD rate not found in /latest?base=IDR");
        }

        double usdRate = ((Number) usdObj).doubleValue();
        double usdBuySpreadIdr = spreadCalculator.usdBuySpreadIdr(usdRate);

        double rounded = BigDecimal.valueOf(usdBuySpreadIdr)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        response.put("USD_BuySpread_IDR", rounded);

        // Store as immutable one-element array
        this.loaded = List.of(Map.copyOf(response));
    }

    @Override
    public List<Object> loadedData() {
        return loaded;
    }

    @Override
    public List<Object> getData() {
        List<Object> fromStore = store.getOrNull(TYPE);
        return fromStore != null ? fromStore : List.of();
    }
}
