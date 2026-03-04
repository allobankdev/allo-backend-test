package com.finance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.finance.client.FrankfurterClientFactory;

@Configuration
public class WebClientConfig {

    @Bean
    public FrankfurterClientFactory frankfurterClient() {
        return new FrankfurterClientFactory();
    }
}
