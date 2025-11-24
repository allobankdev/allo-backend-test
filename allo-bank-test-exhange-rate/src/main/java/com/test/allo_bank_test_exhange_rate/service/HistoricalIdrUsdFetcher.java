package com.test.allo_bank_test_exhange_rate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public HistoricalIdrUsdFetcher(@Qualifier("frankfurterWebClientFactory") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Object> fetchData() {
        String path = "/2024-01-01..2024-01-05";
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("base", "IDR")
                        .queryParam("symbols", "USD")
                        .build())
                .retrieve()
                .bodyToMono(Object.class);
    }
    
}
