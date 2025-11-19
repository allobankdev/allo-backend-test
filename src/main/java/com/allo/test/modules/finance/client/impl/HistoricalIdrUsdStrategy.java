package com.allo.test.modules.finance.client.impl;

import com.allo.test.configs.properties.FrankfurterApiProperties;
import com.allo.test.modules.finance.client.IDRDataFetcher;
import com.allo.test.modules.finance.dto.res.HistoricalRateResponse;
import com.allo.test.modules.finance.dto.res.FrankfurterHistoricalRatesResponse;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.service.DataStoreService;
import com.allo.test.shared.utils.WebClientErrorHandler;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Strategy implementation for fetching historical IDR to USD exchange rates
 * for a specific date range.
 * <p>
 * Handles the "historical_idr_usd" resource type and transforms data to List of
 * HistoricalRateResponse DTOs for type-safe JSON output.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HistoricalIdrUsdStrategy implements IDRDataFetcher {

    private final FrankfurterApiProperties apiProperties;
    private final DataStoreService dataStoreService;

    @Override
    @Retry(name = "frankfurterApi")
    public List<HistoricalRateResponse> fetchData(WebClient webClient) {
        String dateRange = apiProperties.getHistoricalRates().getDateRange();
        String fromCurrency = apiProperties.getHistoricalRates().getFromCurrency();
        String toCurrency = apiProperties.getHistoricalRates().getToCurrency();

        log.info("Fetching historical rates from {} to {} for date range: {}",
                fromCurrency, toCurrency, dateRange);

        FrankfurterHistoricalRatesResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/" + dateRange)
                        .queryParam("from", fromCurrency)
                        .queryParam("to", toCurrency)
                        .build())
                .retrieve()
                .bodyToMono(FrankfurterHistoricalRatesResponse.class)
                .onErrorMap(e -> WebClientErrorHandler.mapException(e, "/" + dateRange))
                .block();

        // Transform to List of HistoricalRateResponse DTOs
        List<HistoricalRateResponse> transformedData = transformToList(response);

        // Store in DataStore
        if (!transformedData.isEmpty()) {
            dataStoreService.store(getResourceType(), transformedData);
        }

        return transformedData;
    }

    /**
     * Transforms the raw HistoricalRatesResponse to List of HistoricalRateResponse DTOs.
     * Provides type-safe structure for JSON array output.
     */
    private List<HistoricalRateResponse> transformToList(FrankfurterHistoricalRatesResponse response) {
        if (response == null || response.getRates() == null) {
            return Collections.emptyList();
        }

        List<HistoricalRateResponse> transformedList = new ArrayList<>();

        for (Map.Entry<LocalDate, Map<String, BigDecimal>> entry : response.getRates().entrySet()) {
            LocalDate date = entry.getKey();
            Map<String, BigDecimal> currencyRates = entry.getValue();

            for (Map.Entry<String, BigDecimal> currencyEntry : currencyRates.entrySet()) {
                HistoricalRateResponse rateResponse = HistoricalRateResponse.builder()
                        .date(date.toString())
                        .currency(currencyEntry.getKey())
                        .rate(currencyEntry.getValue())
                        .build();

                transformedList.add(rateResponse);
            }
        }

        return transformedList;
    }

    @Override
    public Object getData() {
        return dataStoreService.get(getResourceType());
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.HISTORICAL_RATES;
    }
}
