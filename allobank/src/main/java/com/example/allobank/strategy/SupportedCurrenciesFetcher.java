package com.example.allobank.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetch() {
        String url = "https://api.frankfurter.app/currencies";
        return restTemplate.getForObject(url, Object.class);
    }
}
