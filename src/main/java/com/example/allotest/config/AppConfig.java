package com.example.allotest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    private final AppProp properties;

    public AppConfig(AppProp properties) {
        this.properties = properties;
    }

    @Bean
    public WebClient webClient() throws Exception {
        return new WebClientFactoryBean(properties).getObject();
    }
}
