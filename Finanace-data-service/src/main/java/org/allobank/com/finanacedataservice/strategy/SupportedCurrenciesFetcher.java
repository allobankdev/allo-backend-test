package org.allobank.com.finanacedataservice.strategy;

import org.allobank.com.finanacedataservice.clent.FrankfurterClient;
import org.allobank.com.finanacedataservice.domain.SupportedCurrenciesResult;
import org.allobank.com.finanacedataservice.service.FinanceDataCache;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(SupportedCurrenciesFetcher.RESOURCE_TYPE)
public class SupportedCurrenciesFetcher implements IdrDataFetcher {
    public static final String RESOURCE_TYPE = "supported_currencies";

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public void loadData(FrankfurterClient client, FinanceDataCache cache) {
        Map<String, String> response = client.getSupportedCurrencies();
        List<SupportedCurrenciesResult> results = new ArrayList<>();
        if (response != null) {
            for (Map.Entry<String, String> entry : response.entrySet()) {
                results.add(new SupportedCurrenciesResult(entry.getKey(), entry.getValue()));
            }
        }
        cache.setSupportedCurrencies(results);
    }

    @Override
    public List<?> getCachedData(FinanceDataCache cache) {
        return cache.getSupportedCurrencies();
    }
}
