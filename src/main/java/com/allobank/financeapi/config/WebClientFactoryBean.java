package com.allobank.financeapi.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component("frankfurterWebClient")
public class WebClientFactoryBean implements FactoryBean<WebClient> {

    @Value("${api.frankfurter.baseUrl}")
    private String baseUrl;

    @Override
    public WebClient getObject() throws Exception {
        return WebClient.builder()
                .baseUrl(this.baseUrl)
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
