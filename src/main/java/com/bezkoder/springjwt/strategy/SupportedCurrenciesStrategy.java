package com.bezkoder.springjwt.strategy;

import com.bezkoder.springjwt.client.FrankfurterApiClient;
import com.bezkoder.springjwt.store.FinanceDataStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SupportedCurrenciesStrategy implements IDRDataFetcherStrategy {

    private static final String TYPE = "supported_currencies";

    private final FrankfurterApiClient apiClient;
    private final FinanceDataStore store;

    private volatile List<Object> loaded = List.of();

    public SupportedCurrenciesStrategy(FrankfurterApiClient apiClient, FinanceDataStore store) {
        this.apiClient = apiClient;
        this.store = store;
    }

    @Override
    public String resourceType() {
        return TYPE;
    }

    @Override
    public void loadAtStartup() {
        Map<String, String> response = apiClient.getCurrencies();

        List<Object> list = new ArrayList<>();
        for (Map.Entry<String, String> e : response.entrySet()) {
            list.add(Map.of(
                    "code", e.getKey(),
                    "name", e.getValue()
            ));
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
