package com.tes.allo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private final FrankfurterProperties frankfurterProperties;

    public WebClientConfig(FrankfurterProperties frankfurterProperties) {
        this.frankfurterProperties = frankfurterProperties;
    }

    @Bean
    public WebClientFactoryBean frankfurterWebClientFactoryBean() {
        return new WebClientFactoryBean(frankfurterProperties);
    }

    @Bean
    @Primary
    public WebClient frankfurterWebClient(WebClientFactoryBean factory) throws Exception {
        return factory.getObject();
    }
}
