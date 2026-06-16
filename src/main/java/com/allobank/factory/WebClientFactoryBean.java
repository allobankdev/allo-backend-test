package com.allobank.factory;

import com.allobank.config.FrankfurterApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class WebClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterApiProperties apiProperties;

    @Override
    public WebClient getObject() {
        return WebClient.builder()
                .baseUrl(apiProperties.getBaseUrl())
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)) // 16MB
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

