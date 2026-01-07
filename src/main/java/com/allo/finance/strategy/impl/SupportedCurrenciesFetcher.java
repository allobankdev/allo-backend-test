package com.allo.finance.strategy.impl;

import com.allo.finance.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final WebClient frankfurterWebClient;

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetchData() {
        return frankfurterWebClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

}