package com.allobank.finance.strategy;

import com.allobank.finance.model.SupportedCurrenciesData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final RestClient client;

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public SupportedCurrenciesData fetchData() {
        log.debug("Fetching supported currencies");

        Map<String, String> currencies = client.get()
                .uri("/currencies")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        Assert.notNull(currencies, "Failed to fetch supported currencies");

        return SupportedCurrenciesData.builder()
                .currencies(currencies)
                .count(currencies.size())
                .build();
    }
}
