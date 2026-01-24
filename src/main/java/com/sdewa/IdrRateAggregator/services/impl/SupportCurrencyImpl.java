package com.sdewa.IdrRateAggregator.services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.sdewa.IdrRateAggregator.dtoes.SupportCurrencyRecord;
import com.sdewa.IdrRateAggregator.services.IDRDataFetcher;

@Service
public class SupportCurrencyImpl implements IDRDataFetcher<List<SupportCurrencyRecord>> {
    private final WebClient webClient;

    public SupportCurrencyImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public List<SupportCurrencyRecord> fetchData() {

        Map<String, String> currencies = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
                })
                .block();

        List<SupportCurrencyRecord> resultList = currencies.entrySet().stream().map(x -> {
            return SupportCurrencyRecord.builder()
                    .country(x.getValue())
                    .currency(x.getKey())
                    .build();
        }).toList();

        return resultList;
    }
}
