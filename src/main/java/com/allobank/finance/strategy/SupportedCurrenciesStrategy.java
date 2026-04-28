package com.allobank.finance.strategy;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private static final Logger log = Logger.getLogger(SupportedCurrenciesStrategy.class.getName());
    private static final String RESOURCE_TYPE = "supported_currencies";

    private final WebClient webClient;

    public SupportedCurrenciesStrategy(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<Map<String, Object>> fetch() {
        log.info("[SupportedCurrenciesStrategy] Fetching supported currencies...");

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.get()
                    .uri("/currencies")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null) {
                throw new IllegalStateException("Response null dari Frankfurter API");
            }

            return transformResponse(response);

        } catch (WebClientResponseException ex) {
            log.severe("[SupportedCurrenciesStrategy] HTTP error: " + ex.getStatusCode());
            throw new RuntimeException("Gagal fetch currencies: HTTP " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            log.severe("[SupportedCurrenciesStrategy] Error: " + ex.getMessage());
            throw new RuntimeException("Gagal fetch currencies", ex);
        }
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    private List<Map<String, Object>> transformResponse(Map<String, Object> response) {
        List<Map<String, Object>> results = new ArrayList<>();

        for (Map.Entry<String, Object> entry : response.entrySet()) {
            Map<String, Object> currencyEntry = new LinkedHashMap<>();
            currencyEntry.put("code", entry.getKey());
            currencyEntry.put("name", entry.getValue());
            results.add(currencyEntry);
        }

        results.sort((a, b) ->
                String.valueOf(a.get("code")).compareTo(String.valueOf(b.get("code"))));

        return results;
    }
}