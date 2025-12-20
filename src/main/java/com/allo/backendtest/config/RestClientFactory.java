package com.allo.backendtest.config;

import com.allo.backendtest.dto.properties.FrankfurterProperties;
import com.allo.backendtest.exception.HttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Slf4j
public final class RestClientFactory implements FactoryBean<RestClient> {

    private final FrankfurterProperties properties;
    private final ObjectMapper objectMapper;

    public RestClientFactory(FrankfurterProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public RestClient getObject() {
        var requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectionRequestTimeout(properties.connectTimeoutMillis());
        requestFactory.setReadTimeout(properties.readTimeoutMillis());

        var factory = new BufferingClientHttpRequestFactory(requestFactory);

        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .requestFactory(factory)
                .requestInterceptor(new LoggingInterceptor())
                .defaultHeader("Content-Type", "application/json")
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    log.error("Error HttpResponse : {}", objectMapper.writeValueAsString(response));
                    throw new HttpException(response.getStatusCode().value(), "The server is down.");
                })
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return RestClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}