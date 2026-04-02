package com.example.idrapi.strategy.impl;

import com.example.idrapi.config.FrankfurterProperties;
import com.example.idrapi.dto.HistoricalRatesResponse;
import com.example.idrapi.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class HistoricalIDRUSDFetcher implements IDRDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(HistoricalIDRUSDFetcher.class);
    private static final String RESOURCE_TYPE = "historical_idr_usd";

    private final WebClient webClient;
    private final String startDate;
    private final String endDate;

    public HistoricalIDRUSDFetcher(WebClient webClient, FrankfurterProperties properties) {
        this.webClient = webClient;
        this.startDate = properties.getHistorical().getStartDate();
        this.endDate   = properties.getHistorical().getEndDate();
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public List<Map<String, Object>> fetch() {
        String uri = String.format("/%s..%s?from=IDR&to=USD", startDate, endDate);
        log.debug("Fetching historical IDR/USD rates from: {}", uri);

        HistoricalRatesResponse response = webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(body -> new RuntimeException(
                                        "Frankfurter API error [" + clientResponse.statusCode() + "]: " + body))
                )
                .bodyToMono(HistoricalRatesResponse.class)
                .block();

        if (response == null || response.getRates() == null) {
            throw new IllegalStateException("Received null response from Frankfurter historical endpoint");
        }

        List<Map<String, Object>> results = new ArrayList<>();
        response.getRates().forEach((date, currencies) -> {
            Map<String, Object> record = new LinkedHashMap<>();
            record.put("date",       date);
            record.put("base",       response.getBase());
            record.put("startDate",  response.getStartDate());
            record.put("endDate",    response.getEndDate());
            record.put("USD",        currencies.get("USD"));
            results.add(record);
        });

        log.debug("Fetched {} historical records", results.size());
        return results;
    }
}
