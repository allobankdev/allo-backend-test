package com.allo.test.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientFactoryBean implements FactoryBean<WebClient> {
    @Value("${external.api.base-url}")
    private String baseUrl;
    private WebClient webClient;

    @Override
    public WebClient getObject(){
        if (webClient == null){
            webClient = WebClient.builder()
                    .baseUrl(baseUrl)
                    .build();
        }
        return webClient;
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
