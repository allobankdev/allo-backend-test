package com.allo.test.modules.finance.client.impl;

import com.allo.test.configs.properties.FrankfurterApiProperties;
import com.allo.test.modules.finance.client.IDRDataFetcher;
import com.allo.test.modules.finance.dto.res.LatestIDRRatesResponse;
import com.allo.test.modules.finance.dto.res.FrankfurterLatestRatesResponse;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.exceptions.ResponseParsingException;
import com.allo.test.modules.finance.service.DataStoreService;
import com.allo.test.shared.utils.SpreadCalculator;
import com.allo.test.shared.utils.WebClientErrorHandler;
import io.github.resilience4j.retry.annotation.Retry;
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
@Component
public class LatestIdrRatesStrategy implements IDRDataFetcher {

    private final FrankfurterApiProperties apiProperties;
    private final DataStoreService dataStoreService;
    private final double spreadFactor;

    public LatestIdrRatesStrategy(FrankfurterApiProperties apiProperties, DataStoreService dataStoreService) {
        this.apiProperties = apiProperties;
        this.dataStoreService = dataStoreService;
        this.spreadFactor = SpreadCalculator.calculateSpreadFactor(apiProperties.getGithubUsername());
        log.info("Initialized LatestIDRRatesStrategy with spread factor: {}", spreadFactor);
    }

    @Override
    @Retry(name = "frankfurterApi")
    public LatestIDRRatesResponse fetchData(WebClient webClient) {
        String endpoint = apiProperties.getLatestRates().getEndpoint();
        String baseCurrency = apiProperties.getLatestRates().getBaseCurrency();

        log.info("Fetching latest rates with base currency: {}", baseCurrency);

        // Fetch base rates from API with error mapping
        FrankfurterLatestRatesResponse baseResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(endpoint)
                        .queryParam("base", baseCurrency)
                        .build())
                .retrieve()
                .bodyToMono(FrankfurterLatestRatesResponse.class)
                .onErrorMap(e -> WebClientErrorHandler.mapException(e, endpoint))
                .block();

        // Validation
        if (baseResponse == null || baseResponse.getRates() == null) {
            log.error("Failed to fetch latest rates or rates map is null");
            throw new ResponseParsingException(endpoint);
        }

        // Get USD rate from the response
        BigDecimal usdRate = baseResponse.getRates().get("USD");

        // Calculate USD_BuySpread_IDR
        BigDecimal usdBuySpreadIdr = SpreadCalculator.calculateUsdBuySpread(usdRate, spreadFactor);

        // Build enhanced response
        LatestIDRRatesResponse response = LatestIDRRatesResponse.fromLatestRatesResponse()
                .base(baseResponse)
                .usdBuySpreadIdr(usdBuySpreadIdr)
                .build();

        // Store in DataStore
        if (response != null) {
            dataStoreService.store(getResourceType(), response);
        }

        return response;
    }

    @Override
    public Object getData() {
        return dataStoreService.get(getResourceType());
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.LATEST_RATES;
    }
}
