package com.springboot.test.service;

import com.springboot.test.config.CurrencyApiConfig;
import com.springboot.test.dto.HistoricalDTO;
import com.springboot.test.dto.LatestRateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    private final RestTemplate restTemplate;
    private final CurrencyApiConfig config;

    @Autowired
    public ApiService(RestTemplate restTemplate, CurrencyApiConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    @Cacheable("getDataLatestRate")
    public LatestRateDTO getDataLatestRate() {
        String url = config.getLatestRateUrl();
        return restTemplate.getForObject(url, LatestRateDTO.class);
    }

    @Cacheable("getHistoricalIdrUsd")
    public HistoricalDTO getHistoricalIdrUsd() {
        String url = config.getHistoricalUrl();
        return restTemplate.getForObject(url, HistoricalDTO.class);
    }

    @Cacheable("getSupportedCurrencies")
    public String getSupportedCurrencies() {
        String url = config.getSupportedCurrenciesUrl();
        return restTemplate.getForObject(url, String.class);
    }
}
