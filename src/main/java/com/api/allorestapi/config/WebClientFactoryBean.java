package com.api.allorestapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Component("frankfurterWebClient")
public class WebClientFactoryBean implements FactoryBean<WebClient> {

    @Value("${frankfurter.base-url}")
    private String baseUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public WebClient getObject() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(config -> {
                    config.defaultCodecs().jackson2JsonDecoder(
                            new Jackson2JsonDecoder(objectMapper));
                    config.defaultCodecs().jackson2JsonEncoder(
                            new Jackson2JsonEncoder(objectMapper));
                    config.defaultCodecs().maxInMemorySize(1024 * 1024);
                })
                .build();

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/json")
                .exchangeStrategies(strategies)
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}