
package com.allo_backend_test.finance.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Component
public class FrankfurterRestTemplateFactoryBean implements FactoryBean<RestTemplate> {

    @Value("${frankfurter.base-url}")
    private String baseUrl;

    @Value("${frankfurter.connect-timeout}")
    private long connectTimeout;

    @Value("${frankfurter.read-timeout}")
    private long readTimeout;

    private RestTemplate restTemplate;

    @Override
    public RestTemplate getObject() {
        if (restTemplate == null) {
            restTemplate = new RestTemplateBuilder()
                    .rootUri(baseUrl)
                    .setConnectTimeout(Duration.ofMillis(connectTimeout))
                    .setReadTimeout(Duration.ofMillis(readTimeout))
                    .build();
        }
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
