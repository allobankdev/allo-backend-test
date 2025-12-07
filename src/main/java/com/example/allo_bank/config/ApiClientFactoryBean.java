package com.example.allo_bank.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class ApiClientFactoryBean implements FactoryBean<RestTemplate> {

    private final ApiClientProperties properties;

    public ApiClientFactoryBean(ApiClientProperties properties) {
        this.properties = properties;
    }

    @Override
    public RestTemplate getObject() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectionRequestTimeout(5000);
        factory.setReadTimeout(5000);

        RestTemplate restTemplate = new RestTemplate(factory);

        restTemplate.setUriTemplateHandler(
                new org.springframework.web.util.DefaultUriBuilderFactory(properties.getBaseUrl())
        );

        return restTemplate;
    }

    @Override
    public Class<?> getObjectType() {
        return RestTemplate.class;
    }

    @Bean
    public ApiClientFactoryBean restTemplate(ApiClientProperties props) {
        return new ApiClientFactoryBean(props);
    }


}
