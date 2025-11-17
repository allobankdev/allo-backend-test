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

import java.util.Map;

@Slf4j
@Component(CurrencyConstant.LATEST_IDR_RATES)
public class LatestRatesStrategy implements CurrencyStrategy {

    private static final String USERNAME = "AriAulia";
    private final RestTemplate restTemplate;
    private final ExternalApiConfig externalApiConfig;

    @Autowired
    public LatestRatesStrategy(RestTemplate restTemplate, ExternalApiConfig externalApiConfig) {
        this.restTemplate = restTemplate;
        this.externalApiConfig = externalApiConfig;
    }

    @Cacheable(value = "latestIdrUsd", key = "'latestIdrUsd'", unless = "#result == null")
    @Override
    public CurrencyResponseDto execute() {
        log.info("Executing latest currency logic");
        String url = externalApiConfig.getBaseUrl() + "/latest?base=IDR";
        FrankfurterResponseDto latestRate = this.restTemplate.getForObject(url, FrankfurterResponseDto.class);

        CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto();
        currencyResponseDto.setBase(latestRate.getBase());
        currencyResponseDto.setDate(latestRate.getDate());
        currencyResponseDto.setRates(latestRate.getRates());
        currencyResponseDto.setUsdBuySpreadIdr(calculateBuySpread(latestRate));
        return currencyResponseDto;
    }

    private double calculateBuySpread(FrankfurterResponseDto latestRate) {
        int asciiUserName = 0;
        for (char c : USERNAME.toLowerCase().toCharArray()) {
            asciiUserName += c;
        }
        double spreadFactor = (asciiUserName % 1000) / 100000.0;
        double rateUSD = (double) ((Map) latestRate.getRates()).get("USD");
        return (1 / rateUSD) * (1 + spreadFactor);
    }

}
