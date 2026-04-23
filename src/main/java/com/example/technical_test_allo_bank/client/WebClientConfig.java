package com.example.technical_test_allo_bank.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient frankfurterWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.frankfurter.app")
                .build();
    }
}
