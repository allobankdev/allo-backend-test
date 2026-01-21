package com.allo.app.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.allo.app.dto.FrankfurterProperties;
import com.allo.app.dto.response.HistoricalIdrUsdResponse;
import com.allo.app.service.IDRDataFetcher;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HistoricalIdrUsd implements IDRDataFetcher<HistoricalIdrUsdResponse> {

    private final WebClient webClient;
    private final FrankfurterProperties frankfurterProperties;

    @Override
    public Mono<HistoricalIdrUsdResponse> getData() {
        return webClient.get()
                .uri(frankfurterProperties.getUrl() + "/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalIdrUsdResponse.class);
    }

}
