package com.allobank.allobanktest.strategy;

import com.allobank.allobanktest.dto.HistoricalRateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public HistoricalIdrUsdFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return ResourceType.HISTORICAL_IDR_USD.getValue();
    }

    @Override
    public Object fetchAndTransform() {
        log.info("Fetching historical IDR to USD exchange rates");

        try {
            HistoricalRateResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/2024-01-01..2024-01-05")
                            .queryParam("from", "IDR")
                            .queryParam("to", "USD")
                            .build())
                    .retrieve()
                    .bodyToMono(HistoricalRateResponse.class)
                    .block();

            log.info("Successfully fetched historical IDR to USD exchange rates");
            return response;

        } catch (Exception ex) {
            log.error("Failed to fetch historical IDR to USD exchange rates", ex);
            throw ex;
        }
    }
}
