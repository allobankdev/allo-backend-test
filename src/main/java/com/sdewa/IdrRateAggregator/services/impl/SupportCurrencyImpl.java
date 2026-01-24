package com.sdewa.IdrRateAggregator.services.impl;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import com.sdewa.IdrRateAggregator.dtoes.SupportCurrency;
import com.sdewa.IdrRateAggregator.services.IDRDataFetcher;


@Service
public class SupportCurrencyImpl implements IDRDataFetcher<SupportCurrency> {
    private final WebClient webClient;

    public SupportCurrencyImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public SupportCurrency fetchData() {

        Map<String, String> currencies = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block(); 

 
        SupportCurrency result = SupportCurrency.builder()
                .supportCurrency(currencies)
                .build();

        return result;
    }
}
