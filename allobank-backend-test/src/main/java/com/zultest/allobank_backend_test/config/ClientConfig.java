package com.zultest.allobank_backend_test.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FrankfurterApiProperties.class)
public class ClientConfig {

    @Bean
    public FrankfurterClientBean frankfurterWebClient(FrankfurterApiProperties properties) {
        return new FrankfurterClientBean(properties);
    }
}
