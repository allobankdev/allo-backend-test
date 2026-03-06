package com.finance.aggregator.config;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class FrankfurterClientConfig {

    @Value("${api.frankfurter.base-url}")
    private String baseUrl;

    @Bean
    public FactoryBean<WebClient> webClientFactoryBean() {
        return new FactoryBean<>() {
            @Override
            public WebClient getObject() {
                return WebClient.builder()
                        .baseUrl(baseUrl)
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
        };
    }
}
