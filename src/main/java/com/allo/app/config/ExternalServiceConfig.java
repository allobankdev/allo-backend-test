package com.allo.app.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.allo.app.dto.FrankfurterProperties;

@Configuration
@EnableConfigurationProperties(FrankfurterProperties.class)
public class ExternalServiceConfig {

    @Bean
    public FrankfurterWebClientFactoryBean frankfurterWebClient(
            FrankfurterProperties properties) {
        return new FrankfurterWebClientFactoryBean(properties);
    }
}
