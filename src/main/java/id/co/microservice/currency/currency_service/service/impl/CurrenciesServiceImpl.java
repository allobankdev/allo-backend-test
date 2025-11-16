package id.co.microservice.currency.currency_service.service.impl;

import id.co.microservice.currency.currency_service.config.ExternalApiConfig;
import id.co.microservice.currency.currency_service.constant.CurrencyConstant;
import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import id.co.microservice.currency.currency_service.service.CurrencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Slf4j
@Component(CurrencyConstant.SUPPORTED_CURRENCIES)
public class CurrenciesServiceImpl implements CurrencyStrategy {

    private final RestTemplate restTemplate;
    private final ExternalApiConfig externalApiConfig;

    @Autowired
    public CurrenciesServiceImpl(RestTemplate restTemplate, ExternalApiConfig externalApiConfig) {
        this.restTemplate = restTemplate;
        this.externalApiConfig = externalApiConfig;
    }

    @Override
    public CurrencyResponseDto execute() {
        log.info("Executing supported currency logic");
        String url = externalApiConfig.getBaseUrl() + "/currencies";
        HashMap<String, String> currencies = this.restTemplate.getForObject(url, HashMap.class);
        CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto();
        currencyResponseDto.setCurrencies(currencies);

        return currencyResponseDto;
    }

}
