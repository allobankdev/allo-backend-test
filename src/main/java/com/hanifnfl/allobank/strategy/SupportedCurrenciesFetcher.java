package com.hanifnfl.allobank.strategy;

import com.hanifnfl.allobank.dto.CurrencyView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private volatile List<CurrencyView> cache = List.of();

    @Override
    public String getResourceTypeKey() {
        return "supported_currencies";
    }

    @Override
    public void loadData(WebClient client) {
        log.info("Fetching supported currencies...");

        Map<String, String> response = client.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

        if (response == null) {
            throw new IllegalStateException("No currencies returned.");
        }

        List<CurrencyView> views = response.entrySet().stream()
                .map(e -> new CurrencyView(e.getKey(), e.getValue()))
                .sorted((a, b) -> a.symbol().compareToIgnoreCase(b.symbol()))
                .toList();

        this.cache = List.copyOf(views);
        log.info("supported_currencies loaded: {} currencies", cache.size());
    }

    @Override
    public List<CurrencyView> getCachedData() {
        return cache;
    }
}
