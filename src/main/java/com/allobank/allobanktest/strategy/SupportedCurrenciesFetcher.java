package com.allobank.allobanktest.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public SupportedCurrenciesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return ResourceType.SUPPORTED_CURRENCIES.getValue();
    }

    @Override
    public Map<String, String> fetchAndTransform() {
        log.info("Fetching supported currencies from Frankfurter API");

        try {
            Map<String, String> currencies = webClient.get()
                    .uri("/currencies")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            log.info("Successfully fetched {} supported currencies",
                    currencies != null ? currencies.size() : 0);

            return currencies;

        } catch (Exception ex) {
            log.error("Failed to fetch supported currencies", ex);
            throw ex;
        }


    }
}
