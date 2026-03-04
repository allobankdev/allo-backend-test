package com.finance.client;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.reactive.function.client.WebClient;

public class FrankfurterClientFactory implements FactoryBean<WebClient>{

    @Override
    public WebClient getObject() {
        return WebClient.builder()
            .baseUrl("https://api.frankfurter.app")
            .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    @Override
    public boolean isSingleton(){
        return true;
    }
}
