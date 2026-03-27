package com.allobank.allo_backend_test.finance.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSourceClient {

    private final RestClient restClient;

    public Map<String, Object> getLatestRates(String base) {
        return restClient.get()
                .uri(u -> u.path("/latest").queryParam("base", base).build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Map<String, Object> getHistoricalRates(String startDate, String endDate, String from, String to) {
        return restClient.get()
                .uri(u -> u.path("/{range}")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .build(startDate + ".." + endDate))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Map<String, Object> getCurrencies() {
        return restClient.get()
                .uri("/currencies")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}