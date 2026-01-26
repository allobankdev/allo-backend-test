package com.chnh16.backendtest.resttemplate;

import com.chnh16.backendtest.config.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Component
@RequiredArgsConstructor
public class RestTemplateFactory implements FactoryBean<RestTemplate> {

    private final ApplicationConfig applicationConfig;

    @Override
    public RestTemplate getObject() throws Exception {
        HttpComponentsClientHttpRequestFactory requestFactory
                = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectionRequestTimeout(applicationConfig.getTimeoutMs());
        requestFactory.setReadTimeout(applicationConfig.getTimeoutMs());
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        DefaultUriBuilderFactory uriBuilderFactory
                = new DefaultUriBuilderFactory(applicationConfig.getBaseUrl());
        restTemplate.setUriTemplateHandler(uriBuilderFactory);
        restTemplate.getInterceptors().add((r, b, e) -> {
            r.getHeaders().add("X-Client", "allo-backend-test");
            return e.execute(r, b);
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
