package com.allo.backendtest.config;

import com.allo.backendtest.dto.properties.FrankfurterProperties;
import com.allo.backendtest.dto.properties.GithubProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableConfigurationProperties({FrankfurterProperties.class, GithubProperties.class})
public class ClientConfig {

    @Bean
    public RestClientFactory frankfurterRestClient(FrankfurterProperties properties, ObjectMapper objectMapper) {
        return new RestClientFactory(properties, objectMapper);
    }
}