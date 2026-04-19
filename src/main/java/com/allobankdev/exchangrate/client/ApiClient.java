package com.allobankdev.exchangrate.client;

import com.allobankdev.exchangrate.dto.CurrencyResponse;
import com.allobankdev.exchangrate.dto.HistoricalResponse;
import com.allobankdev.exchangrate.dto.LatestRateResponse;
import com.allobankdev.exchangrate.util.RetryUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiClient {
    private final RestTemplate restTemplate;

    private final static String LATEST_RATES_ENDPOINT = "/latest?base-IDR";
    private final static String HISTORICAL_RATES_ENDPOINT = "/2024-01-01..2024-01-05?from=IDR&to=USD";
    private final static String CURRENCIES_ENDPOINT = "/currencies";

    @Value("${external.api.base-url}")
    private String baseUrl;

    public ApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LatestRateResponse getLatestRates() {
        return RetryUtil.executeWithRetry(
                () -> restTemplate.getForObject(
                baseUrl + LATEST_RATES_ENDPOINT,
                LatestRateResponse.class
        ), 3, 1000);
    }

    public HistoricalResponse getHistoricalRates() {
        return RetryUtil.executeWithRetry(
                () -> restTemplate.getForObject(
                baseUrl + HISTORICAL_RATES_ENDPOINT,
                HistoricalResponse.class
                ), 3, 1000);
    }

    public CurrencyResponse getCurrencies() {
        return RetryUtil.executeWithRetry(
                () -> restTemplate.getForObject(
                        baseUrl + CURRENCIES_ENDPOINT,
                        CurrencyResponse.class
                ), 3, 1000);
    }

}
