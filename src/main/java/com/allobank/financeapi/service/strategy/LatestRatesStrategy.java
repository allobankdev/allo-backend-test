package com.allobank.financeapi.service.strategy;

import com.allobank.financeapi.model.dto.LatestRatesResponse;
import com.allobank.financeapi.model.enums.ResourceType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class LatestRatesStrategy implements DataFetcherStrategy {

    private final WebClient webClient;

    public LatestRatesStrategy(@Qualifier("frankfurterWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    private static final double SPREAD_FACTOR = 0.00842; // (sum(ord(c) for c in "rakuszz0") % 1000) / 100000.0

    @Override
    public ResourceType getResourceType() {
        return ResourceType.LATEST_IDR_RATES;
    }

    @Override
    public Mono<Object> fetchData() {
        return this.webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .doOnNext(this::calculateSpread)
                .map(data -> (Object) data);
    }

    private void calculateSpread(LatestRatesResponse response) {
        if (response.getRates() != null && response.getRates().containsKey("USD")) {
            double rateUsd = response.getRates().get("USD");
            double spread = (1 / rateUsd) * (1 + SPREAD_FACTOR);
            response.setUSD_BuySpread_IDR(spread);
        }
    }
}
