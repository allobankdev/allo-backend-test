package org.allobanktest.strategy;

import org.allobanktest.dto.SupportedCurrencyItem;
import org.allobanktest.store.FinancialDataStore;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {
    @Override
    public String resourceKey() {
        return "supported_currencies";
    }

    @Override
    public List<?> load(WebClient webClient, String githubUsername) {
        Map<String, String> raw = webClient.get()
                .uri("/currencies")
                .retrieve().bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(5))
                .block();
        List<SupportedCurrencyItem> items = new ArrayList<>();
        assert raw != null;
        raw.forEach((code, name) -> items.add(new SupportedCurrencyItem(code, name)));

        return List.copyOf(items);
    }

    @Override
    public List<?> getCached(FinancialDataStore store) {
        return store.getSupportedCurrencies();
    }
}
