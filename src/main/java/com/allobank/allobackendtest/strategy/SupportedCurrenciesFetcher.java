package com.allobank.allobackendtest.strategy;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final WebClient frankfurterWebClient;

    public SupportedCurrenciesFetcher(WebClient frankfurterWebClient) {
        this.frankfurterWebClient = frankfurterWebClient;
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetchData() {
        return frankfurterWebClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();
    }

}
