package org.allobanktest.strategy;

import org.allobanktest.dto.HistoricalUsdItem;
import org.allobanktest.store.FinancialDataStore;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {
    static class HistoricalRaw {
        public double amount;
        public String base;
        public String start_date;
        public String end_date;
        public Map<String, Map<String, Double>> rates;
    }

    @Override
    public String resourceKey() {
        return "historical_idr_usd";
    }

    @Override
    public List<?> load(WebClient webClient, String githubUsername) {
        HistoricalRaw raw = webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve().bodyToMono(HistoricalRaw.class)
                .timeout(Duration.ofSeconds(5)).block();

        List<HistoricalUsdItem> items = new ArrayList<>();
        assert raw != null;
        raw.rates.forEach((date, map) -> {
            Double usd = map.get("USD");
            if (usd != null) items.add(new HistoricalUsdItem(date, usd));
        });

        return List.copyOf(items);
    }

    @Override
    public List<?> getCached(FinancialDataStore store) {
        return store.getHistoricalIdrUsd();
    }
}
