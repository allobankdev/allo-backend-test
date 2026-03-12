package com.allobank.financeapi.factory;

import com.allobank.financeapi.config.FrankfurterApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class WebClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterApiProperties properties;

    @Override
    public WebClient getObject() {
        // Configure WebClient with base URL from properties
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .codecs(config -> config.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true; // Reuse same WebClient instance
    }
}