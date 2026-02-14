package com.allo.test.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component("frankfurtRestTemplate")
public class FrankfurtRestTemplateFactoryBean implements FactoryBean<RestTemplate> {

    private final FrankfurterProperties properties;

    public FrankfurtRestTemplateFactoryBean(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public RestTemplate getObject() {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getTimeout().getConnect());
        factory.setReadTimeout(properties.getTimeout().getRead());

        RestTemplate restTemplate = new RestTemplate(factory);

        DefaultUriBuilderFactory uriFactory =
                new DefaultUriBuilderFactory(properties.getBaseUrl());

        restTemplate.setUriTemplateHandler(uriFactory);
        restTemplate.getInterceptors().add(loggingInterceptor());

        return restTemplate;
    }

    private ClientHttpRequestInterceptor loggingInterceptor() {
        return (HttpRequest request, byte[] body, ClientHttpRequestExecution execution) -> {

            Instant start = Instant.now();
            log.info("Calling API: {} {}", request.getMethod(), request.getURI());

            ClientHttpResponse response = execution.execute(request, body);

            Instant end = Instant.now();
            long duration = Duration.between(start, end).toMillis();

            log.info("Response: {} | Duration: {} ms",
                    response.getStatusCode(), duration);

            return response;
        };
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
