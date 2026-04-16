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
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public SupportedCurrenciesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<Map<String, Object>> fetchData() {
        Map<String, Object> response = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> currencies = Optional.ofNullable(response)
                .orElseThrow(() -> new FinanceDataLoadException("Frankfurter currencies response was empty"));

        return currencies.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
                .map(entry -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("code", entry.getKey());
                    row.put("name", entry.getValue());
                    return Map.copyOf(row);
                })
                .toList();
    }
}
