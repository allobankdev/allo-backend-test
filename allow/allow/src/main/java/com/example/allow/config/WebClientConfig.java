package com.example.allow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public Duration webClientTimeout(@Value("${webclient.timeout-seconds:10}") long seconds) {
        return Duration.ofSeconds(seconds);
    }
}