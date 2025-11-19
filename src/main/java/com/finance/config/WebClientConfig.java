package com.finance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient frankClient() {
        return WebClient.builder()
                .baseUrl("https://api.frankfurter.app")
                .build();
    }
}
