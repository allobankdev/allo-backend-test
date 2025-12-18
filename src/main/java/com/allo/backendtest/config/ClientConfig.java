package com.allo.backendtest.config;

import com.allo.backendtest.dto.FrankfurterProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FrankfurterProperties.class)
public class ClientConfig {

    @Bean
    public FrankfurterRcBean frankfurterRestClientConfig(FrankfurterProperties properties) {
        return new FrankfurterRcBean(properties);
    }
}