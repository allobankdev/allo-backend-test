package com.example.allobank.strategy;

import com.example.allobank.client.SpreadCalculator;
import com.example.allobank.dto.LatestRatesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("latest_idr_rates")
@RequiredArgsConstructor
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final WebClient client;
    private final SpreadCalculator spreadCalculator;

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public List<?> fetchData() {
        LatestRatesResponse response = client.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .block();

        double usdRate = response.getRates().get("USD");
        double spread = spreadCalculator.getSpreadFactor();

        double usdBuySpreadIdr = (1 / usdRate) * (1 + spread);

        Map<String, Object> result = new HashMap<>();
        result.put("date", response.getDate());
        result.put("usd_rate", usdRate);
        result.put("USD_BuySpread_IDR", usdBuySpreadIdr);

        return List.of(result);
    }
}