package com.allobank.backend.test.config;

import com.allobank.backend.test.client.FrankfurterClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class FrankfurterClientFactoryBean implements FactoryBean<FrankfurterClient> {

    private final FrankfurterApiProperties properties;

    @Override
    public FrankfurterClient getObject() {
        RestTemplate restTemplate = new RestTemplate();
        return new FrankfurterClient(restTemplate, properties);
    }

    @Override
    public Class<?> getObjectType() {
        return FrankfurterClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}