package com.example.allobank.backend.test.takehometest.fetcher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class LatestIdrRatesFetcher implements DataFetcher{

    private final WebClient webClient;

    public LatestIdrRatesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "latest_rates_fetcher";
    }

    @Override
    public Object fetchData() {
        return webClient
            .get()
            .uri("/latest?base=IDR")
            .retrieve()
            .bodyToMono(Object.class)
            .block();
    }

}
