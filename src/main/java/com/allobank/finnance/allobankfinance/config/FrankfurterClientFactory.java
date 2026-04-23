package com.allobank.finnance.allobankfinance.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FrankfurterClientFactory implements FactoryBean<RestTemplate> {

    private final FrankfurterProperties properties;

    public FrankfurterClientFactory(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public RestTemplate getObject() {
        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());

        RestTemplate restTemplate = new RestTemplate(factory);

        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("X-Client", "AlloBank-Finance");
            return execution.execute(request, body);
        });
        return restTemplate;
    }

    @Override
    public Class<?> getObjectType() {
        return RestTemplate.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
