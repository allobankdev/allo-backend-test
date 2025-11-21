package com.chikohakles.allobank.agregator.config;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientFactoryBean implements FactoryBean<RestClient> {
    @Value("${frankfurt.api}")
    private String baseUrl;

    @Value("${frankfurt.connect-timeout-ms:3000}")
    private int connectTimeout;

    @Value("${frankfurt.read-timeout-ms:5000}")
    private int readTimeout;

    @Override
    public @Nullable RestClient getObject() throws Exception {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return RestClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
