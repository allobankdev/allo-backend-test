package com.allobank.finace.config;

import com.allobank.finace.properties.FrankfurterProperties;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
public class FrankfurterWebClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterProperties properties;

    @Autowired
    public FrankfurterWebClientFactoryBean(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public WebClient getObject() {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .codecs(config -> config.defaultCodecs().maxInMemorySize(1024 * 1024))
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
