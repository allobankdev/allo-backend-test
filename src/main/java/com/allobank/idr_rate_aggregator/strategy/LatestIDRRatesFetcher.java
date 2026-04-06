package com.allobank.idr_rate_aggregator.strategy;

import com.allobank.idr_rate_aggregator.config.FrankfurterProperties;
import com.allobank.idr_rate_aggregator.config.SpreadProperties;
import com.allobank.idr_rate_aggregator.dto.LatestRatesResponse;
import com.allobank.idr_rate_aggregator.model.FinanceData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LatestIDRRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;
    private final FrankfurterProperties properties;
    private final SpreadProperties spreadProperties;

    @Override
    public FinanceData fetch() {
        log.info("Fetching latest IDR rates...");

        Map<String, Object> rawRates = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(properties.getEndpoints().getLatest())
                        .queryParam("base", properties.getParams().getBase())
                        .build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException(
                                        "External API error: " + response.statusCode() + " - " + body)))
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (rawRates == null) {
            throw new RuntimeException("Empty response from Frankfurter API for latest IDR rates");
        }

        return FinanceData.ofLatestRates(transformLatestRates(rawRates));
    }
    
    private Map<String, Object> transformLatestRates(Map<String, Object> raw) {
        Map<String, Object> result = new HashMap<>(raw);

        Object ratesObj = raw.get("rates");
        if (ratesObj instanceof Map<?, ?> rates && rates.containsKey("USD")) {
            double rateUsd = ((Number) rates.get("USD")).doubleValue();
            double spreadFactor = spreadProperties.getFactor();

            double usdBuySpreadIdr = (1.0 / rateUsd) * (1.0 + spreadFactor);
            result.put("USD_BuySpread_IDR", usdBuySpreadIdr);

            log.info("Calculated USD_BuySpread_IDR: {} (spreadFactor: {})",
                    usdBuySpreadIdr, spreadFactor);
        }

        return result;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }
}