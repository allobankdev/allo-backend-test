package id.co.microservice.currency.currency_service.service.impl;

import id.co.microservice.currency.currency_service.config.ExternalApiConfig;
import id.co.microservice.currency.currency_service.constant.CurrencyConstant;
import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import id.co.microservice.currency.currency_service.dto.FrankfurterResponseDto;
import id.co.microservice.currency.currency_service.service.CurrencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component(CurrencyConstant.HISTORICAL_IDR_USD)
public class HistoricalServiceImpl implements CurrencyStrategy {

    private final RestTemplate restTemplate;
    private final ExternalApiConfig externalApiConfig;

    @Autowired
    public HistoricalServiceImpl(RestTemplate restTemplate, ExternalApiConfig externalApiConfig) {
        this.restTemplate = restTemplate;
        this.externalApiConfig = externalApiConfig;
    }

    @Cacheable(value = "historicalIdrUsd", key = "'historicalIdrUsd'", unless = "#result == null")
    @Override
    public CurrencyResponseDto execute() {
        log.info("Executing history currency logic");
        String url = externalApiConfig.getBaseUrl() + "/2024-01-01..2024-01-05?from=IDR&to=USD";
        FrankfurterResponseDto historicalRate = this.restTemplate.getForObject(url, FrankfurterResponseDto.class);

        CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto();
        currencyResponseDto.setBase(historicalRate.getBase());
        currencyResponseDto.setDate(historicalRate.getDate());
        currencyResponseDto.setRates(historicalRate.getRates());
        currencyResponseDto.setStartDate(historicalRate.getStartDate());
        currencyResponseDto.setEndDate(historicalRate.getEndDate());

        return currencyResponseDto;
    }

}
