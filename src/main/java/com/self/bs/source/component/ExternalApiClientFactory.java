package com.self.bs.source.component;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.self.bs.source.config.ExchangeRateProperties;

@Component
public class ExternalApiClientFactory implements FactoryBean<WebClient>{
    @Autowired
    protected ExchangeRateProperties exchangeRateProperties;

    @Override
    public WebClient getObject() {
        return WebClient.builder()
            .baseUrl(exchangeRateProperties.getExternalUrl())
            .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }
}
