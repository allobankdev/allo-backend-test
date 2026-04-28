package com.allobankdev.allo_idr_rate_aggregator.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.allobankdev.allo_idr_rate_aggregator.model.dto.FrankfurterLatestResponse;
import com.allobankdev.allo_idr_rate_aggregator.service.SpreadCalculator;
import com.allobankdev.allo_idr_rate_aggregator.strategy.IDRDataFetcher;

@Component
@RequiredArgsConstructor
public class LatestIDRRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;
    private final SpreadCalculator spreadCalculator;
    

    @Override
    public String resourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Mono<List<Object>> fetch() {
        return webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(FrankfurterLatestResponse.class)
                .log()
                .map(resp -> {
                    // Map<String, Double> rates = (Map<String, Double>) resp.get("rates");
                    double usdRate = resp.getRates().get("USD");
                    double spread = spreadCalculator.spreadFactor();

                    double usdBuySpread = (1 / usdRate) * (1 + spread);

                    Map<String, Object> rateData = new HashMap<>();
                    rateData.put("base", "IDR");
                    rateData.put("rates", resp.getRates());
                    rateData.put("USD_BuySpread_IDR", usdBuySpread);
                    // ... masukkan field lainnya satu per satu dengan .put()

                    return Collections.singletonList(rateData);
                });
    }
}

