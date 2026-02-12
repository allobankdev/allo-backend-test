package com.mlutfiazizan13.allo_backend_test.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Component
public class RestTemplateFactoryBean implements FactoryBean<RestTemplate> {

    private final ApiClientProperties properties;
    private final LoggingInterceptor loggingInterceptor;

    public RestTemplateFactoryBean(ApiClientProperties properties,
                                   LoggingInterceptor loggingInterceptor) {
        this.properties = properties;
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public RestTemplate getObject() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.getConnectionTimeout());
        requestFactory.setReadTimeout(properties.getReadTimeout());

        return new RestTemplateBuilder()
                .requestFactory(() -> new BufferingClientHttpRequestFactory(requestFactory))
                .rootUri(properties.getBaseUrl())
                .connectTimeout(Duration.ofMillis(properties.getConnectionTimeout()))
                .readTimeout(Duration.ofMillis(properties.getReadTimeout()))
                .defaultHeader("Accept", properties.getHeaders().getAccept())
                .defaultHeader("Content-Type", properties.getHeaders().getContentType())
                .interceptors(loggingInterceptor)
                .build();
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
