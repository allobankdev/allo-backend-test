package com.allo.test.modules.finance.client.strategy.impl;

import com.allo.test.modules.finance.client.strategy.IDRDataFetcher;
import com.allo.test.modules.finance.dto.res.LatestIDRRatesResponse;
import com.allo.test.modules.finance.dto.res.LatestRatesResponse;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.store.DataStore;
import com.allo.test.shared.utils.SpreadCalculator;
import com.allo.test.configs.properties.FrankfurterApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

/**
 * Strategy implementation for fetching latest IDR exchange rates
 * and calculating the USD buy spread with banking margin.
 * <p>
 * Handles the "latest_idr_rates" resource type.
 */
@Slf4j
@Component("latest_idr_rates")
public class LatestIDRRatesStrategy implements IDRDataFetcher {

    private static final String ENDPOINT = "/latest";
    private static final String BASE_CURRENCY = "IDR";

    private final FrankfurterApiProperties apiProperties;
    private final DataStore dataStore;
    private final double spreadFactor;

    public LatestIDRRatesStrategy(FrankfurterApiProperties apiProperties, DataStore dataStore) {
        this.apiProperties = apiProperties;
        this.dataStore = dataStore;
        this.spreadFactor = SpreadCalculator.calculateSpreadFactor(apiProperties.getGithubUsername());
        log.info("Initialized LatestIDRRatesStrategy with spread factor: {}", spreadFactor);
    }

    @Override
    public LatestIDRRatesResponse fetchData(WebClient webClient) {
        log.info("Fetching latest rates with base currency: {}", BASE_CURRENCY);

        // Fetch base rates from API
        LatestRatesResponse baseResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ENDPOINT)
                        .queryParam("base", BASE_CURRENCY)
                        .build())
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .doOnSuccess(response -> log.info("Successfully fetched latest rates for {} currencies",
                        response.getRates() != null ? response.getRates().size() : 0))
                .doOnError(error -> log.error("Error fetching latest rates: {}", error.getMessage()))
                .block();

        if (baseResponse == null || baseResponse.getRates() == null) {
            log.error("Failed to fetch latest rates or rates map is null");
            return null;
        }

        // Get USD rate from the response
        BigDecimal usdRate = baseResponse.getRates().get("USD");
        if (usdRate == null) {
            log.error("USD rate not found in response");
            return null;
        }

        // Calculate USD_BuySpread_IDR
        BigDecimal usdBuySpreadIdr = SpreadCalculator.calculateUsdBuySpread(usdRate, spreadFactor);
        log.info("Calculated USD_BuySpread_IDR: {} (from USD rate: {}, spread factor: {})",
                usdBuySpreadIdr, usdRate, spreadFactor);

        // Build enhanced response
        LatestIDRRatesResponse response = LatestIDRRatesResponse.fromLatestRatesResponse()
                .base(baseResponse)
                .usdBuySpreadIdr(usdBuySpreadIdr)
                .build();

        // Store in DataStore
        if (response != null) {
            dataStore.store(getResourceType(), response);
            log.debug("Stored latest IDR rates in DataStore");
        }

        return response;
    }

    @Override
    public Object getData() {
        return dataStore.get(getResourceType());
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.LATEST_RATES;
    }
}
