package com.example.allo_bank.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SupportedCurrencies extends BaseDataFetcher{

    @Value("${frankfurter.api.base-url}")
    private String baseUrl;

//    public SupportedCurrencies(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }

    public SupportedCurrencies(
            RestTemplate restTemplate,
            @Value("${frankfurter.api.base-url}") String baseUrl
    ) {
        super(restTemplate, baseUrl);
    }

    @Override
    public String getResourceName() {
        return "supported_currencies";
    }

    @Override
    public Object fetchData() {
        String url = baseUrl + "/currencies";
        return restTemplate.getForObject(url, Object.class);
    }
}
