package com.example.idrapi.strategy.impl;

import com.example.idrapi.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(SupportedCurrenciesFetcher.class);
    private static final String RESOURCE_TYPE = "supported_currencies";

    private final WebClient webClient;

    public SupportedCurrenciesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public List<Map<String, Object>> fetch() {
        log.debug("Fetching supported currencies from Frankfurter API...");

        Map<String, String> currencyMap = webClient.get()
                .uri("/currencies")
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(body -> new RuntimeException(
                                        "Frankfurter API error [" + clientResponse.statusCode() + "]: " + body))
                )
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

        if (currencyMap == null) {
            throw new IllegalStateException("Received null response from Frankfurter /currencies endpoint");
        }

        // Transform { "USD": "US Dollar" } → [ { code: "USD", name: "US Dollar" }, ... ]
        List<Map<String, Object>> results = new ArrayList<>();
        currencyMap.forEach((code, name) -> {
            Map<String, Object> record = new LinkedHashMap<>();
            record.put("code", code);
            record.put("name", name);
            results.add(record);
        });

        log.debug("Fetched {} supported currencies", results.size());
        return results;
    }
}
