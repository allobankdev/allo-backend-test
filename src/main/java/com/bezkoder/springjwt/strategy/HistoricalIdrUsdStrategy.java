package com.bezkoder.springjwt.strategy;

import com.bezkoder.springjwt.client.FrankfurterApiClient;
import com.bezkoder.springjwt.store.FinanceDataStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class HistoricalIdrUsdStrategy implements IDRDataFetcherStrategy {

    private static final String TYPE = "historical_idr_usd";

    private final FrankfurterApiClient apiClient;
    private final FinanceDataStore store;

    private volatile List<Object> loaded = List.of();

    public HistoricalIdrUsdStrategy(FrankfurterApiClient apiClient, FinanceDataStore store) {
        this.apiClient = apiClient;
        this.store = store;
    }

    @Override
    public String resourceType() {
        return TYPE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadAtStartup() {
        Map<String, Object> response = apiClient.getHistoricalIdrToUsd();

        Object baseObj = response.get("base");
        String base = baseObj != null ? String.valueOf(baseObj) : "IDR";

        Object ratesObj = response.get("rates");
        if (!(ratesObj instanceof Map)) {
            throw new IllegalStateException("Invalid response format from historical endpoint");
        }

        Map<String, Object> ratesByDate = (Map<String, Object>) ratesObj;

        List<Object> list = new ArrayList<>();
        for (Map.Entry<String, Object> e : ratesByDate.entrySet()) {
            String date = e.getKey();
            Object inner = e.getValue();

            if (!(inner instanceof Map)) {
                continue;
            }
            Map<String, Object> innerRates = (Map<String, Object>) inner;

            // Output per-day object: {date, base, rates:{USD:...}}
            Map<String, Object> out = Map.of(
                    "date", date,
                    "base", base,
                    "rates", Map.copyOf(innerRates)
            );
            list.add(out);
        }

        this.loaded = List.copyOf(list);
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
