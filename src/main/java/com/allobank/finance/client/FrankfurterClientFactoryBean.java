package com.allobank.finance.client;

import com.allobank.finance.config.FinanceProperties;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.reactive.function.client.WebClient;

public class FrankfurterClientFactoryBean implements FactoryBean<FrankfurterClient> {

    private final FinanceProperties financeProperties;

    public FrankfurterClientFactoryBean(FinanceProperties financeProperties) {
        this.financeProperties = financeProperties;
    }

    @Override
    public FrankfurterClient getObject() throws Exception {
        WebClient webClient = WebClient.builder().baseUrl(financeProperties.getBaseUrl()).build();

        return new FrankfurterClient(webClient);
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
