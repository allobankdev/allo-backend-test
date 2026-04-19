package com.allobankdev.exchangrate.config;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class RestTemplateFactory implements FactoryBean<RestTemplate> {

    @Override
    public @Nullable RestTemplate getObject() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setErrorHandler(new ResTemplateErrorHandler());

        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Accept", "application/json");
            log.info("Outgoing request: {} {}", request.getMethod(), request.getURI());
            return execution.execute(request, body);
        });
        return restTemplate;
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return RestTemplate.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
