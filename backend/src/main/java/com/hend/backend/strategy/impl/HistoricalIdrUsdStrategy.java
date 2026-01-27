package com.hend.backend.strategy.impl;

import com.hend.backend.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author : hend wunga
 */

@Component("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalIdrUsdStrategy implements IDRDataFetcher {

    private final WebClient webClient;


    @Override
    public Object fetchData() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Object.class) // Menggunakan Object untuk fleksibilitas JSON array/map
                .block();
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }
}
