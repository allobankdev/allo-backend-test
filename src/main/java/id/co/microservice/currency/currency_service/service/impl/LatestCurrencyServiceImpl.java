package id.co.microservice.currency.currency_service.service.impl;

import id.co.microservice.currency.currency_service.config.ExternalApiConfig;
import id.co.microservice.currency.currency_service.constant.CurrencyConstant;
import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import id.co.microservice.currency.currency_service.dto.FrankfurterResponseDto;
import id.co.microservice.currency.currency_service.service.CurrencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component(CurrencyConstant.LATEST_IDR_RATES)
public class LatestCurrencyServiceImpl implements CurrencyStrategy {

    private final RestTemplate restTemplate;
    private final ExternalApiConfig externalApiConfig;

    @Autowired
    public LatestCurrencyServiceImpl(RestTemplate restTemplate, ExternalApiConfig externalApiConfig) {
        this.restTemplate = restTemplate;
        this.externalApiConfig = externalApiConfig;
    }

    @Override
    public CurrencyResponseDto execute() {
        log.info("Executing latest currency logic");
        String url = externalApiConfig.getBaseUrl() + "/latest?base=IDR";
        FrankfurterResponseDto latestRate = this.restTemplate.getForObject(url, FrankfurterResponseDto.class);

        CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto();
        currencyResponseDto.setBase(latestRate.getBase());
        currencyResponseDto.setDate(latestRate.getDate());
        currencyResponseDto.setRates(latestRate.getRates());

        return currencyResponseDto;
    }

}
