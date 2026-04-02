package com.example.idrapi.strategy.impl;

import com.example.idrapi.config.FrankfurterProperties;
import com.example.idrapi.dto.LatestRatesResponse;
import com.example.idrapi.strategy.IDRDataFetcher;
import com.example.idrapi.util.CalculateUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.idrapi.util.CalculateUtil.calculateSpreadFactor;

@Component
public class LatestIDRRatesFetcher implements IDRDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(LatestIDRRatesFetcher.class);
    private static final String RESOURCE_TYPE = "latest_idr_rates";

    private final WebClient webClient;
    private final double spreadFactor;

    public LatestIDRRatesFetcher(WebClient webClient, FrankfurterProperties properties) {
        this.webClient    = webClient;
        this.spreadFactor = calculateSpreadFactor(properties.getGithubUsername());
        log.info("Spread factor for username '{}': {}", properties.getGithubUsername(), this.spreadFactor);
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public List<Map<String, Object>> fetch() {
        log.debug("Fetching latest IDR rates from Frankfurter API...");

        LatestRatesResponse response = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(body -> new RuntimeException(
                                        "Frankfurter API error [" + clientResponse.statusCode() + "]: " + body))
                )
                .bodyToMono(LatestRatesResponse.class)
                .block();

        if (response == null || response.getRates() == null) {
            throw new IllegalStateException("Received null response from Frankfurter /latest endpoint");
        }

        Double usdRate = response.getRates().get("USD");
        if (usdRate == null || usdRate == 0) {
            throw new IllegalStateException("USD rate not present or zero in latest IDR rates response");
        }

        double usdBuySpreadIDR = (1.0 / usdRate) * (1.0 + spreadFactor);
        log.debug("Calculated USD_BuySpread_IDR = {}", usdBuySpreadIDR);

        // Build result map preserving insertion order
        Map<String, Object> record = new LinkedHashMap<>();
        record.put("base",              response.getBase());
        record.put("date",              response.getDate());
        record.put("rates",             response.getRates());
        record.put("spreadFactor",      spreadFactor);
        record.put("USD_BuySpread_IDR", usdBuySpreadIDR);

        return List.of(record);
    }

}
