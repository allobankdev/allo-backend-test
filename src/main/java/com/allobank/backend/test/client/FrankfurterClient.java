package com.allobank.backend.test.client;

import com.allobank.backend.test.config.FrankfurterApiProperties;
import com.allobank.backend.test.model.CurrenciesResponse;
import com.allobank.backend.test.model.HistoricalRatesResponse;
import com.allobank.backend.test.model.LatestRatesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class FrankfurterClient {

    private final RestTemplate restTemplate;
    private final FrankfurterApiProperties properties;

    public LatestRatesResponse getLatestRates() {
        String url = properties.getBaseUrl() + properties.getLatest();
        return restTemplate.getForObject(url, LatestRatesResponse.class);
    }

    public CurrenciesResponse getCurrencies() {
        String url = properties.getBaseUrl() + properties.getCurrencies();
        return restTemplate.getForObject(url, CurrenciesResponse.class);
    }

    public HistoricalRatesResponse getHistoricalRates() {
        String url = properties.getBaseUrl() + properties.getHistorical();
        return restTemplate.getForObject(url, HistoricalRatesResponse.class);
    }
}