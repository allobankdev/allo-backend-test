package com.allobank.service;

import com.allobank.config.FrankfurterApiProperties;
import com.allobank.dto.LatestRatesResponse;
import com.allobank.dto.LatestRatesWithSpreadResponse;
import com.allobank.enums.ResourceType;
import com.allobank.store.DataStoreService;
import com.allobank.utility.SpreadCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("latest_idr_rates")
@RequiredArgsConstructor
@Slf4j
public class LatestIDRRatesStrategy implements IDRDataFetcher {

    private final WebClient webClient;
    private final SpreadCalculator spreadCalculator;
    private final FrankfurterApiProperties properties;
    private final DataStoreService dataStoreService;

    @Override
    public Object fetchFromExternalApi() {
        log.info("Fetching latest IDR rates from: {}", properties.getEndpoints().getLatestIdr());

        LatestRatesResponse response = webClient.get()
                .uri(properties.getEndpoints().getLatestIdr())
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .block();

        if (response == null || response.getRates() == null) {
            throw new RuntimeException("Failed to fetch latest rates");
        }

        return transformWithSpread(response);
    }

    @Override
    public Object getData() {
        return dataStoreService.getData(getResourceType().getValue());
    }

    /**
     * Transforms the response by calculating the USD buy spread
     */
    private LatestRatesWithSpreadResponse transformWithSpread(LatestRatesResponse response) {
        BigDecimal usdRate = response.getRates().get("USD");

        if (usdRate == null || usdRate.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("USD rate not found or invalid");
        }

        BigDecimal spreadFactor = spreadCalculator.calculateSpreadFactor();

        // Formula: USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
        BigDecimal usdBuySpreadIdr = BigDecimal.ONE
                .divide(usdRate, 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.ONE.add(spreadFactor))
                .setScale(5, RoundingMode.HALF_UP);

        log.info("Calculated USD_BuySpread_IDR: {} with spread factor: {}",
                usdBuySpreadIdr, spreadFactor);

        return LatestRatesWithSpreadResponse.builder()
                .amount(response.getAmount())
                .base(response.getBase())
                .date(response.getDate())
                .rates(response.getRates())
                .usdBuySpreadIdr(usdBuySpreadIdr)
                .spreadFactor(spreadFactor)
                .build();
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.LATEST_IDR_RATES;
    }
}