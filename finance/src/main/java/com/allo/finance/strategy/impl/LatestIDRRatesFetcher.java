package com.allo.finance.strategy.impl;

import com.allo.finance.dto.LatestRatesResponse;
import com.allo.finance.strategy.IDRDataFetcher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
public class LatestIDRRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    private static final double SPREAD_FACTOR = 0.00097;

    public LatestIDRRatesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchData() {
        LatestRatesResponse response = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .block();

        Double usdRate = response.getRates().get("USD");

        double usdBuySpread = (1 / usdRate) * (1 + SPREAD_FACTOR);

        Map<String, Object> result = new HashMap<>();
        result.put("base", response.getBase());
        result.put("date", response.getDate());
        result.put("rates", response.getRates());
        result.put("USD_BuySpread_IDR", usdBuySpread);

        return result;
    }
}
