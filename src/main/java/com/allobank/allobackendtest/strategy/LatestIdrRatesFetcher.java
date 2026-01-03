package com.allobank.allobackendtest.strategy;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import com.allobank.allobackendtest.model.DTO.LatestIdrRatesResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "latest_idr_rates";

    private final WebClient webClient;

    public LatestIdrRatesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public Object fetchData() {
        log.info("Fetching latest IDR rates from Frankfurter API...");
        try {
            LatestIdrRatesResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/latest").queryParam("base", "IDR").build())
                    .retrieve()
                    .bodyToMono(LatestIdrRatesResponse.class)
                    .block();

            log.info("Successfully fetched latest IDR rates");
            return response;

        } catch (WebClientRequestException ex) {
            log.error("Failed to reach Frankfurter API (network issue)", ex);
            throw new IllegalStateException("Failed to fetch latest IDR rates: network/API unreachable",ex);
        } catch (Exception ex) {
            log.error("Unexpected error while fetching latest IDR rates", ex);
            throw new IllegalStateException("Unexpected error while fetching latest IDR rates",ex);
        }
    }

}
