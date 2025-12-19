package com.zultest.allobank_backend_test.service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class LatestIDRRatesFetcher implements IDRDataFetcherInterface {

    private final WebClient webClient;

    public LatestIDRRatesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }


    @Override
    public String resourceType() {
        return "latest_idr_rates";
    }

    @Override
    public List<?> fetchData() {
        Map<String, Object> response = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return List.of(response);
    }
}
