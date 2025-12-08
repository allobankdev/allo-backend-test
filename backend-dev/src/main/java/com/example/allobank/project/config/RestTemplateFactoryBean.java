package com.example.allobank.project.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;


@Component
public class RestTemplateFactoryBean implements FactoryBean<RestTemplate> {

    @Value("${api.frankfurter.base-url}")
    private String baseUrl;

    @Value("${api.frankfurter.connect-timeout:3000}")
    private int connectTimeout;

    @Value("${api.frankfurter.read-timeout:5000}")
    private int readTimeout;

    @Override
    public RestTemplate getObject() {
        if (baseUrl == null || baseUrl.isBlank() || !baseUrl.startsWith("http")) {
            throw new IllegalArgumentException("Invalid Frankfurter API base URL: " + baseUrl);
        }

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));

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
