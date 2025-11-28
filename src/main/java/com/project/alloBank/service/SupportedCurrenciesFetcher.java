package com.project.alloBank.service;

import com.project.alloBank.dto.CurrencyMapResponse;
import com.project.alloBank.repository.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final WebClient webClient;


    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetchData() {
        Map<String, String> response = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();
        CurrencyMapResponse currencyMapResponse = new CurrencyMapResponse();
        currencyMapResponse.setCurrencies(response);
        return currencyMapResponse;
    }
}
