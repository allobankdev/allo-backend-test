package com.project.alloBank.service;

import com.project.alloBank.dto.HistoricalRatesResponse;
import com.project.alloBank.repository.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetchData() {
        Map<String, Object> response = webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        HistoricalRatesResponse dto = new HistoricalRatesResponse();
        dto.setData(response);
        return dto;
    }
}
