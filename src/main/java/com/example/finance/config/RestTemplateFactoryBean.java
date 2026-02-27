package com.example.finance.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateFactoryBean implements FactoryBean<RestTemplate> {

    @Value("${external.api.base-url}")
    private String baseUrl;

    @Value("${external.api.connect-timeout:5000}")
    private int connectTimeout;

    @Value("${external.api.read-timeout:5000}")
    private int readTimeout;

    @Override
    public RestTemplate getObject() throws Exception {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        RestTemplate rt = new RestTemplate(factory);
        // Optionally add interceptors or base URL handling via UriTemplateHandler
        return rt;
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