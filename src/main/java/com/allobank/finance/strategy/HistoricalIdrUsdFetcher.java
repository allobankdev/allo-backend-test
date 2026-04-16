package com.allobank.finance.strategy;

import com.allobank.finance.exception.FinanceDataLoadException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private static final String HISTORY_PATH = "/2024-01-01..2024-01-05";

    private final WebClient webClient;

    public HistoricalIdrUsdFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<Map<String, Object>> fetchData() {
        Map<String, Object> response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(HISTORY_PATH)
                        .queryParam("from", "IDR")
                        .queryParam("to", "USD")
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> historicalRates = Optional.ofNullable(response)
                .orElseThrow(() -> new FinanceDataLoadException("Frankfurter historical rates response was empty"));
        Map<String, Object> ratesByDate = castObjectMap(historicalRates.get("rates"));

        return ratesByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
                .map(entry -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("date", entry.getKey());
                    row.put("base", historicalRates.getOrDefault("base", "IDR"));
                    row.put("rates", entry.getValue());
                    return Map.copyOf(row);
                })
                .toList();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castObjectMap(Object value) {
        if (!(value instanceof Map<?, ?>)) {
            throw new FinanceDataLoadException("Frankfurter historical response did not contain a rates object");
        }
        return (Map<String, Object>) value;
    }
}
