package com.allobank.finance.strategy;

import com.allobank.finance.config.FrankfurterProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class HistoricalIDRUSDStrategy implements IDRDataFetcher {

    private static final Logger log = Logger.getLogger(HistoricalIDRUSDStrategy.class.getName());
    private static final String RESOURCE_TYPE = "historical_idr_usd";

    private final WebClient webClient;
    private final FrankfurterProperties properties;

    public HistoricalIDRUSDStrategy(WebClient webClient, FrankfurterProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }

    @Override
    public List<Map<String, Object>> fetch() {
        String fromDate = properties.getHistorical().getFrom();
        String toDate = properties.getHistorical().getTo();
        String uri = String.format("/%s..%s?from=IDR&to=USD", fromDate, toDate);

        log.info("[HistoricalIDRUSDStrategy] Fetching dari URI: " + uri);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null) {
                throw new IllegalStateException("Response null dari Frankfurter API");
            }

            return transformResponse(response);

        } catch (WebClientResponseException ex) {
            log.severe("[HistoricalIDRUSDStrategy] HTTP error: " + ex.getStatusCode());
            throw new RuntimeException("Gagal fetch historical IDR/USD: HTTP " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            log.severe("[HistoricalIDRUSDStrategy] Error: " + ex.getMessage());
            throw new RuntimeException("Gagal fetch historical IDR/USD", ex);
        }
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> transformResponse(Map<String, Object> response) {
        Map<String, Object> rates = (Map<String, Object>) response.get("rates");
        List<Map<String, Object>> results = new ArrayList<>();

        if (rates == null) {
            log.warning("[HistoricalIDRUSDStrategy] Field 'rates' null");
            return results;
        }

        for (Map.Entry<String, Object> dateEntry : rates.entrySet()) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("date", dateEntry.getKey());
            entry.put("base", response.get("base"));
            entry.put("startDate", response.get("start_date"));
            entry.put("endDate", response.get("end_date"));

            Map<String, Object> dailyRates = (Map<String, Object>) dateEntry.getValue();
            entry.put("rates", dailyRates);

            results.add(entry);
        }

        results.sort((a, b) ->
                String.valueOf(a.get("date")).compareTo(String.valueOf(b.get("date"))));

        return results;
    }
}