package com.allo.idr.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("restTemplate")
public class ApiClientFactory implements FactoryBean<RestTemplate> {
    private final ExternalApiProperties properties;
    private int connectionTimeout;
    private int readTimeout;

    public ApiClientFactory(ExternalApiProperties properties, @Value("${external-api.connection-timeout-ms}")int connectionTimeout, @Value("${external-api.read-timeout-ms}")int readTimeout) {
        this.properties = properties;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
    }

    @Override
    public RestTemplate getObject() throws Exception {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectionTimeout);
        factory.setReadTimeout(readTimeout);
        return new RestTemplate(factory);
    }

    @Override
    public Class<?> getObjectType() {
        return RestTemplate.class;
    }
}
