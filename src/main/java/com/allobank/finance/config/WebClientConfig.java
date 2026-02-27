package com.allobank.finance.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${external.frankfurter.base-url}")
    private String frankfurterBaseUrl;

    @Bean
    public WebClient frankfurterWebClient(ObjectMapper snakeCaseObjectMapper){
        return new WebClientFactory(frankfurterBaseUrl, snakeCaseObjectMapper).getObject();
    }

}
