package com.exchange.frankfurter.strategy;

import com.exchange.frankfurter.service.SpreadCalculator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;
    private final SpreadCalculator spreadCalculator;

    public LatestIdrRatesFetcher(WebClient webClient, SpreadCalculator spreadCalculator) {
        this.webClient = webClient;
        this.spreadCalculator = spreadCalculator;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchData() {
        Map<String, Object> exchangeData = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> Mono.error(new RuntimeException("External API error"))
                )
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> rates = (Map<String, Object>) exchangeData.get("rates");

        Double usdRate = ((Number) rates.get("USD")).doubleValue();

        double spreadFactor = spreadCalculator.calculateSpreadFactor();

        double usdBuySpread = (1 / usdRate) * (1 + spreadFactor);

        exchangeData.put("USD_BuySpread_IDR", usdBuySpread);

        return exchangeData;
    }
}
