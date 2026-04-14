package com.thasya.frankfurter.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FrankfurterWebClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterClientProperties properties;

    public FrankfurterWebClientFactoryBean(FrankfurterClientProperties properties) {
        this.properties = properties;
    }

    @Override
    public WebClient getObject() {
        // di sini kamu bisa tambahkan timeout, default header, dll
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
