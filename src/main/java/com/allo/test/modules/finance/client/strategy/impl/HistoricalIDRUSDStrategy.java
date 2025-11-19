package com.allo.test.modules.finance.client.strategy.impl;

import com.allo.test.configs.properties.FrankfurterApiProperties;
import com.allo.test.modules.finance.client.strategy.IDRDataFetcher;
import com.allo.test.modules.finance.dto.res.HistoricalRatesResponse;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.exceptions.ResponseParsingException;
import com.allo.test.modules.finance.service.DataStoreService;
import com.allo.test.shared.utils.WebClientErrorHandler;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Strategy implementation for fetching historical IDR to USD exchange rates
 * for a specific date range.
 * <p>
 * Handles the "historical_idr_usd" resource type.
 */
@Slf4j
@Component
public class HistoricalIDRUSDStrategy implements IDRDataFetcher {

    private final FrankfurterApiProperties apiProperties;
    private final DataStoreService dataStoreService;

    public HistoricalIDRUSDStrategy(FrankfurterApiProperties apiProperties, DataStoreService dataStoreService) {
        this.apiProperties = apiProperties;
        this.dataStoreService = dataStoreService;
    }

    @Override
    @Retry(name = "frankfurterApi")
    public HistoricalRatesResponse fetchData(WebClient webClient) {
        String dateRange = apiProperties.getHistoricalRates().getDateRange();
        String fromCurrency = apiProperties.getHistoricalRates().getFromCurrency();
        String toCurrency = apiProperties.getHistoricalRates().getToCurrency();

        log.info("Fetching historical rates from {} to {} for date range: {}",
                fromCurrency, toCurrency, dateRange);

        HistoricalRatesResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/" + dateRange)
                        .queryParam("from", fromCurrency)
                        .queryParam("to", toCurrency)
                        .build())
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .onErrorMap(e -> WebClientErrorHandler.mapException(e, "/" + dateRange))
                .block();

        // Store in DataStore
        if (response != null) {
            dataStoreService.store(getResourceType(), response);
            log.debug("Stored historical rates in DataStore");
        }

        return response;
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
