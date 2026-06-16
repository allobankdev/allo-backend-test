package com.example.idr.service.strategy;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.idr.client.dto.LatestRatesResponse;
import com.example.idr.model.LatestIdrResult;
import com.example.idr.service.calculator.SpreadCalculator;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;
    private final SpreadCalculator spreadCalculator;

    public LatestIdrRatesFetcher(WebClient webClient,
                                 SpreadCalculator spreadCalculator) {
        this.webClient = webClient;
        this.spreadCalculator = spreadCalculator;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public List<Object> fetchAndTransform() {
        LatestRatesResponse response = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .block();

        double usdRate = response.getRates().get("USD");
        double spreadFactor = spreadCalculator.calculateSpreadFactor();

        double usdBuySpreadIdr =
                (1 / usdRate) * (1 + spreadFactor);

        return List.of(
                new LatestIdrResult(
                        response.getDate(),
                        response.getRates(),
                        usdBuySpreadIdr
                )
        );
    }
}
