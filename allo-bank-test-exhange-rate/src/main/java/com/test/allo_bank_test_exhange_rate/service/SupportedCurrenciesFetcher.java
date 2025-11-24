package com.test.allo_bank_test_exhange_rate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public SupportedCurrenciesFetcher(@Qualifier("frankfurterWebClientFactory") WebClient webClient) {
        this.webClient = webClient;
    }
    
    @Override
    public Mono<Object> fetchData() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Object.class);
    }
    
}
