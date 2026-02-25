package com.allobank.finance.fetcher;

import com.allobank.finance.dto.FinanceDataResponse;
import com.allobank.finance.dto.FrankfurterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "historical_idr_usd";
    private static final String HISTORICAL_URI = "/2024-01-01..2024-01-05?from=IDR&to=USD";

    private final WebClient webClient;

    @Override
    public FinanceDataResponse fetch() {
        log.info("Fetching historical IDR/USD rates from Frankfurter API...");

        try {
            FrankfurterDto.HistoricalRatesResponse response = webClient.get()
                    .uri(HISTORICAL_URI)
                    .retrieve()
                    .bodyToMono(FrankfurterDto.HistoricalRatesResponse.class)
                    .timeout(Duration.ofSeconds(60))
                    .block();

            if (response == null) {
                throw new IllegalStateException("Empty response from Frankfurter API for historical IDR/USD rates");
            }

            log.info("Historical IDR/USD rates fetched successfully. Date range: {} to {}",
                    response.getStartDate(), response.getEndDate());

            return FinanceDataResponse.builder()
                    .resourceType(RESOURCE_TYPE)
                    .fetchedAt(Instant.now().toString())
                    .data(response)
                    .build();

        } catch (WebClientResponseException ex) {
            log.error("HTTP error fetching historical IDR/USD rates: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch historical IDR/USD rates: HTTP " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            log.error("Unexpected error fetching historical IDR/USD rates", ex);
            throw new RuntimeException("Failed to fetch historical IDR/USD rates", ex);
        }
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }
}
