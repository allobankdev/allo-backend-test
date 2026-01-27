package com.allo.finance.client;

import com.allo.finance.config.FrankfurterProperties;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FrankfurterWebClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterProperties properties;

    public FrankfurterWebClientFactoryBean(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public WebClient getObject() {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
