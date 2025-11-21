package com.chikohakles.allobank.agregator.strategy;

import com.chikohakles.allobank.agregator.constant.ResourceType;
import com.chikohakles.allobank.agregator.service.AgregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CurrenciesStrategy implements BaseStrategy{
    private static final String URL_CURRENCIES = "/currencies";
    private final RestClient restClient;
    @Override
    public ResourceType getResourceType() {
        return ResourceType.SUPPORTED_CURRENCIES;
    }

    @Override
    public Object getData() {
        return restClient.get()
                .uri(URL_CURRENCIES)
                .retrieve()
                .body(List.class);
    }
}
