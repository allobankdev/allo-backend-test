package com.allobank.idrrates.strategy;

import com.allobank.idrrates.dto.HistoricalRateItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class HistoricalIDRUSDFetcher implements IDRDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(HistoricalIDRUSDFetcher.class);

    private final WebClient webClient;

    public HistoricalIDRUSDFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<HistoricalRateItem> fetchAndTransform() {
        log.info("Fetching historical IDR/USD rates from Frankfurter API");

        Map<String, Object> response = webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Map<String, Number>> ratesByDate = (Map<String, Map<String, Number>>) response.get("rates");

        Map<String, Map<String, Number>> sorted = new TreeMap<>(ratesByDate);

        List<HistoricalRateItem> items = new ArrayList<>();
        for (Map.Entry<String, Map<String, Number>> entry : sorted.entrySet()) {
            Map<String, BigDecimal> rateMap = new TreeMap<>();
            for (Map.Entry<String, Number> rateEntry : entry.getValue().entrySet()) {
                rateMap.put(rateEntry.getKey(), new BigDecimal(rateEntry.getValue().toString()));
            }
            items.add(new HistoricalRateItem(entry.getKey(), Collections.unmodifiableMap(rateMap)));
        }

        log.info("Fetched {} historical rate entries", items.size());
        return items;
    }
}
