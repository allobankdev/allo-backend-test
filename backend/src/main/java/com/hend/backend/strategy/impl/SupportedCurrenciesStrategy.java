package com.hend.backend.strategy.impl;

import com.hend.backend.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author : hend wunga
 */

@Component("supported_currencies")
@RequiredArgsConstructor
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public Object fetchData() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }
}
