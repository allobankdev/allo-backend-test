package com.allobank.finance.fetcher;

import com.allobank.finance.dto.FinanceDataResponse;
import com.allobank.finance.dto.FrankfurterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "latest_idr_rates";

    private final WebClient webClient;
    private final SpreadCalculator spreadCalculator;

    @Override
    public FinanceDataResponse fetch() {
        log.info("Fetching latest IDR rates from Frankfurter API...");

        try {
            FrankfurterDto.LatestRatesResponse response = webClient.get()
                    .uri("/latest?base=IDR")
                    .retrieve()
                    .bodyToMono(FrankfurterDto.LatestRatesResponse.class)
                    .timeout(Duration.ofSeconds(180))
                    .block();

            if (response == null || response.getRates() == null) {
                throw new IllegalStateException("Empty response from Frankfurter API for latest IDR rates");
            }

            BigDecimal rateUsd = response.getRates().get("USD");
            if (rateUsd == null || rateUsd.compareTo(BigDecimal.ZERO) == 0) {
                throw new IllegalStateException("USD rate not found or is zero in latest IDR rates response");
            }

            double usdBuySpreadIdr = spreadCalculator.calculateUsdBuySpreadIdr(rateUsd.doubleValue());

            Map<String, Object> enrichedData = new LinkedHashMap<>();
            enrichedData.put("amount", response.getAmount());
            enrichedData.put("base", response.getBase());
            enrichedData.put("date", response.getDate());
            enrichedData.put("rates", response.getRates());

            log.info("Latest IDR rates fetched successfully. USD rate: {}, USD_BuySpread_IDR: {}",
                    rateUsd, usdBuySpreadIdr);

            return FinanceDataResponse.builder()
                    .resourceType(RESOURCE_TYPE)
                    .fetchedAt(Instant.now().toString())
                    .data(enrichedData)
                    .usdBuySpreadIdr(usdBuySpreadIdr)
                    .spreadFactor(spreadCalculator.getSpreadFactor())
                    .build();

        } catch (WebClientResponseException ex) {
            log.error("HTTP error fetching latest IDR rates: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch latest IDR rates: HTTP " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            log.error("Unexpected error fetching latest IDR rates", ex);
            throw new RuntimeException("Failed to fetch latest IDR rates", ex);
        }
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }
}