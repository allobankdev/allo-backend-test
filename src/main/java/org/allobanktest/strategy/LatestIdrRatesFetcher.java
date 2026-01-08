package org.allobanktest.strategy;

import org.allobanktest.dto.LatestRatesItem;
import org.allobanktest.store.FinancialDataStore;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("latest_idr_rates")
public class LatestIdrRatesFetcher implements IDRDataFetcher {
    static class LatestRaw {
        public double amount;
        public String base;
        public String date;
        public Map<String, Double> rates;
    }

    @Override
    public String resourceKey() {
        return "latest_idr_rates";
    }

    @Override
    public List<?> load(WebClient webClient, String githubUsername) {
        LatestRaw raw = webClient.get()
                .uri(uri ->
                        uri.path("/latest").queryParam("base", "IDR").build())
                .retrieve().bodyToMono(LatestRaw.class)
                .timeout(Duration.ofSeconds(5))
                .block();

        assert raw != null;
        double rateUsd = raw.rates.getOrDefault("USD", 0.0);
        double spreadFactor = computeSpreadFactor(githubUsername);
        double usdBuySpreadIdr = rateUsd > 0 ? (1.0 / rateUsd) * (1.0 + spreadFactor) : 0.0;

        List<LatestRatesItem> items = new ArrayList<>();
        raw.rates.forEach((code, rate) -> {
            double spread = "USD".equals(code) ? usdBuySpreadIdr : 0.0;
            items.add(new LatestRatesItem(raw.base, raw.date, code, rate, spread));
        });

        return List.copyOf(items);
    }

    @Override
    public List<?> getCached(FinancialDataStore store) {
        return store.getLatestIdrRates();
    }

    private double computeSpreadFactor(String username) {
        if (username == null) return 0.0;
        int sum = username.toLowerCase().chars().sum();

        return (sum % 1000) / 100000.0;
    }
}
