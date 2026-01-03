package com.allobank.allobackendtest.strategy;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import com.allobank.allobackendtest.model.DTO.HistoricalIdrUsdResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "historical_idr_usd";

    private final WebClient webClient;

    public HistoricalIdrUsdFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public Object fetchData() {
        log.info("Fetching historical IDR-USD rates from Frankfurter API...");
       try {
            HistoricalIdrUsdResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/2024-01-01..2024-01-05")
                            .queryParam("from", "IDR")
                            .queryParam("to", "USD")
                            .build())
                    .retrieve()
                    .bodyToMono(HistoricalIdrUsdResponse.class)
                    .block();

            log.info("Successfully fetched historical IDR-USD rates");
            return response;

        } catch (WebClientRequestException ex) {
            log.error("Network error while fetching historical IDR-USD rates", ex);
            throw new IllegalStateException("Failed to fetch historical IDR-USD rates: network/API unreachable", ex);
        } catch (Exception ex) {
            log.error("Unexpected error while fetching historical IDR-USD rates", ex);
            throw new IllegalStateException("Unexpected error while fetching historical IDR-USD rates", ex);
        }
    }

}
