package com.allobank.finance.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.dto.HistoricalDataResponse;

/**
 * Service for fetching historical IDR to USD rates from Frankfurter API
 */
@Service
public class HistoricalDataService {

    @Value("${frankfurter.historical-date-range:2024-01-01..2024-01-05}")
    private String historicalDateRange;

    private final FrankfurterClient frankfurterClient;

    public HistoricalDataService(FrankfurterClient frankfurterClient) {
        this.frankfurterClient = frankfurterClient;
    }

    /**
     * Fetches historical exchange rates for IDR to USD conversion
     * @return HistoricalDataResponse containing historical rate data
     */
    public HistoricalDataResponse fetchHistoricalData() {
        WebClient webClient = frankfurterClient.getWebClient();

        HistoricalDataResponse response = webClient.get()
            .uri("/" + historicalDateRange + "?from=IDR&to=USD")
            .retrieve()
            .bodyToMono(HistoricalDataResponse.class)
            .block();

        return response;
    }
}
