package org.imam.allo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient(FrankfurterProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.getBaseURL())
                .build();
    }
}
