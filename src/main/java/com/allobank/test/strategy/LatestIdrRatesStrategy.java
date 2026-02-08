package com.allobank.test.strategy;

import com.allobank.test.config.FrankfurterProperties;
import com.allobank.test.model.dto.FrankfurterResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class LatestIdrRatesStrategy implements IdrDataFetcher {

    private final WebClient webClient;
    private final FrankfurterProperties properties;
    private final Double spreadFactor;

    public LatestIdrRatesStrategy(
            WebClient webClient,
            FrankfurterProperties properties,
            @Qualifier("spreadFactorValue") Double spreadFactor) {
        this.webClient = webClient;
        this.properties = properties;
        this.spreadFactor = spreadFactor;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public CompletableFuture<?> fetchData() {
        return webClient.get()
                .uri(properties.getEndpoints().getLatest())
                .retrieve()
                .bodyToMono(FrankfurterResponse.class)
                .map(this::transformData)
                .toFuture();
    }

    private Map<String, Object> transformData(FrankfurterResponse response) {
        Map<String, Object> result = new HashMap<>();
        result.put("base", response.getBase());
        result.put("date", response.getDate());

        Map<String, Double> rates = new HashMap<>(response.getRates());

        if (rates != null && rates.containsKey("USD")) {
            double rateUsd = rates.get("USD");
            double buySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);
            rates.put("USD_BuySpread_IDR", buySpreadIdr);
        }

        result.put("rates", rates);
        return result;
    }
}