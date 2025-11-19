package com.allobank.client;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FrankfurterClientFactoryBean implements FactoryBean<FrankfurterClient> {

    @Override
    public FrankfurterClient getObject() throws Exception {
        return new FrankfurterClient();
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