package com.allobank.allobackendtest.strategy;

import java.time.Duration;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "supported_currencies";

    private final WebClient frankfurterWebClient;

    public SupportedCurrenciesFetcher(WebClient frankfurterWebClient) {
        this.frankfurterWebClient = frankfurterWebClient;
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public Map<String, String> fetchData() {
        log.info("Fetching supported currencies from Frankfurter API...");

        try {
            Map<String, String> result = frankfurterWebClient
                    .get()
                    .uri("/currencies")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            log.info("Successfully fetched {} currencies", result.size());
            return result;

        } catch (WebClientRequestException ex) {
            log.error("Failed to reach Frankfurter API (network issue)", ex);
            throw new IllegalStateException("Failed to fetch supported currencies: network/API unreachable",ex);
        } catch (Exception ex) {
            log.error("Unexpected error while fetching currencies", ex);
            throw ex;
        }
    }

}
