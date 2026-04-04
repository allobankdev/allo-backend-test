package com.allobank.allobank_api.client.frankfurter;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.allobank.allobank_api.config.ExternalApiProperties;

@Component
public class FrankfurterClientFactoryBean implements FactoryBean<FrankfurterClient> {
    private final ExternalApiProperties props;

    public FrankfurterClientFactoryBean(ExternalApiProperties props) {
        this.props = props;
    }

    @Override
    public FrankfurterClient getObject() {
        return new FrankfurterClient(
                WebClient.builder()
                        .baseUrl(props.getFrankfurter().getBaseUrl())
                        .build()
        );
    }

    @Override
    public Class<?> getObjectType() {
        return FrankfurterClient.class;
    }
}
