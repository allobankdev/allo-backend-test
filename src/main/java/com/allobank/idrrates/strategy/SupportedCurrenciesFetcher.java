package com.allobank.idrrates.strategy;

import com.allobank.idrrates.dto.CurrencyItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(SupportedCurrenciesFetcher.class);

    private final WebClient webClient;

    public SupportedCurrenciesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CurrencyItem> fetchAndTransform() {
        log.info("Fetching supported currencies from Frankfurter API");

        Map<String, String> response = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, String> sorted = new TreeMap<>(response);

        List<CurrencyItem> items = new ArrayList<>();
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            items.add(new CurrencyItem(entry.getKey(), entry.getValue()));
        }

        log.info("Fetched {} supported currencies", items.size());
        return items;
    }
}
